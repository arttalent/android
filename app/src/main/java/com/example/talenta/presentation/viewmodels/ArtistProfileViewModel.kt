package com.example.talenta.presentation.viewmodels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.Utilities
import com.example.talenta.data.model.Photo
import com.example.talenta.data.model.Video
import com.example.talenta.data.repository.ArtistRepository
import com.example.talenta.presentation.state.ProfileUiState
import com.example.talenta.presentation.state.UploadState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class ArtistProfileViewModel @Inject constructor(
    private val repository: ArtistRepository, private val utilities: Utilities
) : ViewModel() {
    val firebaseAuth = FirebaseAuth.getInstance()

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _profileState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profileState: StateFlow<ProfileUiState> = _profileState.asStateFlow()

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    private val _photos = mutableStateOf<List<Photo>>(emptyList())
    val photos: State<List<Photo>> = _photos

    private val _videos = mutableStateOf<List<Video>>(emptyList())
    val videos: State<List<Video>> = _videos

    init {
        fetchArtistProfile()
    }

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex

    fun onTabSelected(index: Int) {
        _selectedTabIndex.value = index
    }

    fun fetchArtistProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileUiState.Loading

            try {
                val currentUser = FirebaseAuth.getInstance().currentUser
                    ?: throw Exception("User not authenticated")

                val uid = currentUser.uid
                Log.d("fetchArtistProfile", "Current user UID: $uid")

                val artist = repository.fetchArtistProfile(uid)

                if (artist != null) {
                    Log.d("fetchArtistProfile", "Artist profile fetched: $artist")
                    _profileState.value = ProfileUiState.Success(artist)
                } else {
                    Log.e("fetchArtistProfile", "Artist profile not found for UID: $uid")
                    _profileState.value = ProfileUiState.Error("Artist profile not found")
                }

            } catch (e: Exception) {
                Log.e("fetchArtistProfile", "Error: ${e.message}", e)
                _profileState.value = ProfileUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }


    private suspend fun fetchPhotos(userId: String) {
        try {
            val photosList =
                firestore.collection("artists").document(userId).collection("photos").get()
                    .await().documents.mapNotNull {
                        it.toObject(Photo::class.java)?.copy(id = it.id)
                    }

            _photos.value = photosList
        } catch (e: Exception) {
            // We don't update the overall profile state for this specific error
            // Just keep the photos list empty
        }
    }

    private suspend fun fetchMedia(userId: String) {
        _photos.value = repository.fetchPhotos(userId)
        _videos.value = repository.fetchVideos(userId)
    }

    fun startUpload(imageUri: Uri, description: String, isVideo: Boolean) {
        viewModelScope.launch {
            val downloadUrl = uploadMedia(imageUri, description, isVideo)
            Timber.tag("Upload").d("Download URL: %s", downloadUrl)
        }
    }

    private suspend fun uploadMedia(imageUri: Uri, description: String, isVideo: Boolean): String {
        val userId = utilities.getCurrentUserId()
        val mediaType = if (isVideo) "videos" else "photos"
        val fileId = UUID.randomUUID().toString()
        val fileRef = storage.reference.child("$mediaType/$userId/$fileId")

        fileRef.putFile(imageUri).await()
        val downloadUrl = fileRef.downloadUrl.await().toString()

        val mediaData = if (isVideo) {
            mapOf(
                "id" to fileId,
                "videoUrl" to downloadUrl,
                "description" to description,
                "timestamp" to System.currentTimeMillis()
            )
        } else {
            mapOf(
                "id" to fileId,
                "imageUrl" to downloadUrl,
                "description" to description,
                "timestamp" to System.currentTimeMillis()
            )
        }

        firestore.collection("artists").document(userId).collection(mediaType).document(fileId)
            .set(mediaData).await()

        return downloadUrl
    }


    fun uploadProfilePhoto(imageUri: Uri, callback: (Exception?) -> Unit) {
        viewModelScope.launch {
            try {
                _uploadState.value = UploadState.Loading

                val userId = utilities.getCurrentUserId()
                if (userId.isEmpty()) throw Exception("User not authenticated")

                // Create a reference for the profile image
                val fileRef = storage.reference.child("profile_images/$userId/${UUID.randomUUID()}")

                // Upload the image to Firebase Storage
                fileRef.putFile(imageUri).await()

                // Get the download URL
                val downloadUrl = fileRef.downloadUrl.await().toString()

                // Update profile photo in Firestore using repository function
                repository.updateProfilePhoto(downloadUrl, callback)

                // Refresh artist profile
                fetchArtistProfile()

                _uploadState.value = UploadState.Success(downloadUrl)
            } catch (e: Exception) {
                _uploadState.value =
                    UploadState.Error(e.message ?: "Failed to update profile photo")
            }
        }
    }


    fun deleteMedia(mediaId: String, isVideo: Boolean) {
        viewModelScope.launch {
            try {
                repository.deleteMedia(mediaId, isVideo)
            } catch (e: Exception) {
                // Handling error (e.g., show a Toast or update UI state)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                firebaseAuth.signOut()
                _profileState.value = ProfileUiState.Loading
            } catch (e: Exception) {
                Log.e("ArtistProfileViewModel", "Logout error: ${e.message}", e)
            }
        }
    }

}