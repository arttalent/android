package com.example.talenta.presentation.viewmodels

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.Artist
import com.example.talenta.data.model.Photo
import com.example.talenta.data.model.Video
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID


sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val artist: Artist) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    object Success : UploadState()
    data class Error(val message: String) : UploadState()
}

class ArtistProfileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
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

    fun fetchArtistProfile() {
        viewModelScope.launch {
            try {
                _profileState.value = ProfileUiState.Loading

                val currentUser = auth.currentUser ?: throw Exception("User not authenticated")
                val userId = currentUser.uid

                val artistDoc = firestore.collection("artists").document(userId).get().await()

                if (artistDoc.exists()) {
                    val artist = artistDoc.toObject(Artist::class.java)?.copy(id = userId)
                        ?: throw Exception("Failed to parse artist data")

                    _profileState.value = ProfileUiState.Success(artist)

                    // Also fetch photos and videos separately
                    fetchPhotos(userId)
                    fetchVideos(userId)
                } else {
                    _profileState.value = ProfileUiState.Error("Artist profile not found")
                }
            } catch (e: Exception) {
                _profileState.value = ProfileUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private suspend fun fetchPhotos(userId: String) {
        try {
            val photosList = firestore.collection("artists").document(userId)
                .collection("photos")
                .get().await()
                .documents.mapNotNull { it.toObject(Photo::class.java)?.copy(id = it.id) }

            _photos.value = photosList
        } catch (e: Exception) {
            // We don't update the overall profile state for this specific error
            // Just keep the photos list empty
        }
    }

    private suspend fun fetchVideos(userId: String) {
        try {
            val videosList = firestore.collection("artists").document(userId)
                .collection("videos")
                .get().await()
                .documents.mapNotNull { it.toObject(Video::class.java)?.copy(id = it.id) }

            _videos.value = videosList
        } catch (e: Exception) {
            // We don't update the overall profile state for this specific error
            // Just keep the videos list empty
        }
    }

    fun updateProfilePhoto(imageUri: Uri) {
        viewModelScope.launch {
            try {
                _uploadState.value = UploadState.Loading

                val currentUser = auth.currentUser ?: throw Exception("User not authenticated")
                val userId = currentUser.uid

                // Create a reference to the location where we'll store the file
                val fileRef = storage.reference.child("profile_images/$userId/${UUID.randomUUID()}")

                // Upload the file
                fileRef.putFile(imageUri).await()

                // Get the download URL
                val downloadUrl = fileRef.downloadUrl.await().toString()

                // Update the profile in Firestore
                firestore.collection("artists").document(userId)
                    .update("photoUrl", downloadUrl)
                    .await()

                // Refresh the profile data
                fetchArtistProfile()

                _uploadState.value = UploadState.Success
            } catch (e: Exception) {
                _uploadState.value =
                    UploadState.Error(e.message ?: "Failed to update profile photo")
            }
        }
    }

    fun uploadMedia(imageUri: Uri, description: String, isVideo: Boolean) {
        viewModelScope.launch {
            try {
                _uploadState.value = UploadState.Loading

                val currentUser = auth.currentUser ?: throw Exception("User not authenticated")
                val userId = currentUser.uid

                val mediaType = if (isVideo) "videos" else "photos"
                val fileRef = storage.reference.child("$mediaType/$userId/${UUID.randomUUID()}")

                // Upload the file
                fileRef.putFile(imageUri).await()

                // Get the download URL
                val downloadUrl = fileRef.downloadUrl.await().toString()

                val mediaId = UUID.randomUUID().toString()

                if (isVideo) {
                    // For video we assume the thumbnail is the same as the video for simplicity
                    // In a real app, you'd generate a thumbnail from the video
                    val video = Video(
                        id = mediaId,
                        videoUrl = downloadUrl,
                        thumbnailUrl = downloadUrl, // In real app, generate real thumbnail
                        description = description,
                        timestamp = System.currentTimeMillis()
                    )

                    firestore.collection("artists").document(userId)
                        .collection("videos").document(mediaId)
                        .set(video)
                        .await()

                    fetchVideos(userId)
                } else {
                    val photo = Photo(
                        id = mediaId,
                        imageUrl = downloadUrl,
                        description = description,
                        timestamp = System.currentTimeMillis()
                    )

                    firestore.collection("artists").document(userId)
                        .collection("photos").document(mediaId)
                        .set(photo)
                        .await()

                    fetchPhotos(userId)
                }

                _uploadState.value = UploadState.Success
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Failed to upload media")
            }
        }
    }

    fun updateArtistProfile(updatedArtist: Artist) {
        viewModelScope.launch {
            try {
                _uploadState.value = UploadState.Loading

                val currentUser = auth.currentUser ?: throw Exception("User not authenticated")
                val userId = currentUser.uid

                // Remove ID field since it's the document ID
                val artistData = updatedArtist.copy(id = "")

                // Update the profile in Firestore
                firestore.collection("artists").document(userId)
                    .set(artistData)
                    .await()

                // Refresh the profile data
                fetchArtistProfile()

                _uploadState.value = UploadState.Success
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Failed to update profile")
            }
        }
    }

    fun deleteMedia(mediaId: String, isVideo: Boolean) {
        viewModelScope.launch {
            try {
                _uploadState.value = UploadState.Loading

                val currentUser = auth.currentUser ?: throw Exception("User not authenticated")
                val userId = currentUser.uid

                val mediaType = if (isVideo) "videos" else "photos"

                // First get the media item to get the file URL
                val mediaDoc = firestore.collection("artists").document(userId)
                    .collection(mediaType).document(mediaId)
                    .get().await()

                if (mediaDoc.exists()) {
                    // Get the file URL to delete from storage
                    val fileUrl = if (isVideo) {
                        mediaDoc.getString("videoUrl")
                    } else {
                        mediaDoc.getString("imageUrl")
                    }

                    // Delete from Firestore
                    firestore.collection("artists").document(userId)
                        .collection(mediaType).document(mediaId)
                        .delete()
                        .await()

                    // Try to delete from Storage if URL exists
                    fileUrl?.let {
                        try {
                            // Extract the path from the URL
                            val storageRef = storage.getReferenceFromUrl(it)
                            storageRef.delete().await()
                        } catch (e: Exception) {
                            // If storage deletion fails, we still continue since we removed from Firestore
                        }
                    }

                    // Refresh the media list
                    if (isVideo) {
                        fetchVideos(userId)
                    } else {
                        fetchPhotos(userId)
                    }
                }

                _uploadState.value = UploadState.Success
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Failed to delete media")
            }
        }
    }
}