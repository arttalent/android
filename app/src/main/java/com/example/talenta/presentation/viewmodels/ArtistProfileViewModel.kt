package com.example.talenta.presentation.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.MediaType
import com.example.talenta.data.model.User
import com.example.talenta.data.repository.ArtistRepository
import com.example.talenta.data.repository.ExpertRepository
import com.example.talenta.utils.FirestoreResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val uploadMediaState: UploadMediaState = UploadMediaState.None,
    val selectedTabIndex: Int = 0,
)

sealed class UploadMediaState {
    object None : UploadMediaState()
    object Loading : UploadMediaState()
    object Success : UploadMediaState()
    data class Error(val message: String) : UploadMediaState()
}

@HiltViewModel
class ArtistProfileViewModel @Inject constructor(
    private val repository: ArtistRepository,
    private val expertRepository: ExpertRepository,
) : ViewModel() {
    val firebaseAuth = FirebaseAuth.getInstance()

    private val _profileState = MutableStateFlow<ProfileUiState>(ProfileUiState())
    val profileState: StateFlow<ProfileUiState> = _profileState.asStateFlow()


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
            expertRepository.getCurrentUserProfile(true).collectLatest {
                Log.d("TAG", "fetchArtistProfile: " + it)
                it?.let { user ->
                    _profileState.update { state ->
                        state.copy(
                            user = user,
                        )
                    }
                } ?: run {
                    _profileState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = "Failed to load profile"
                        )
                    }
                }
            }
        }
    }

    fun startUpload(imageUri: Uri, description: String, mediaType: MediaType) {
        viewModelScope.launch {
            uploadMedia(imageUri, description, mediaType)
        }
    }

    fun changeProfilePicture(imageUri: Uri) {
        viewModelScope.launch {
            _profileState.update {
                it.copy(isLoading = true)
            }
            val result = expertRepository.uploadProfilePicture(imageUri)
            when (result) {
                is FirestoreResult.Success -> {
                    fetchArtistProfile()
                    _profileState.update {
                        it.copy(isLoading = false)
                    }
                }

                is FirestoreResult.Failure -> {
                    _profileState.update {
                        it.copy(
                            isLoading = false,
                            error = result.errorMessage ?: "Failed to change profile picture"
                        )
                    }
                }
            }
        }
    }

    private suspend fun uploadMedia(imageUri: Uri, description: String, mediaType: MediaType) {
        _profileState.update {
            it.copy(uploadMediaState = UploadMediaState.Loading)
        }
        val uploadMediaRes = expertRepository.uploadMedia(
            imageUri = imageUri,
            description = description,
            mediaType = mediaType
        )
        when (uploadMediaRes) {
            is FirestoreResult.Failure -> {
                _profileState.update {
                    it.copy(
                        uploadMediaState = UploadMediaState.Error(
                            uploadMediaRes.errorMessage ?: "Upload failed"
                        )
                    )
                }
            }

            is FirestoreResult.Success -> {
                fetchArtistProfile()
                _profileState.update {
                    it.copy(uploadMediaState = UploadMediaState.Success)
                }
                delay(500)
                _profileState.update {
                    it.copy(uploadMediaState = UploadMediaState.None)
                }
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

    fun deleteService(serviceId: String) {
        _profileState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            val result = expertRepository.deleteService(serviceId)

            when (result) {
                is FirestoreResult.Failure -> {
                    _profileState.update {
                        it.copy(isLoading = false, error = result.errorMessage)
                    }
                }

                is FirestoreResult.Success -> {
                    fetchArtistProfile()
                    _profileState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                firebaseAuth.signOut()
            } catch (e: Exception) {
                Log.e("ArtistProfileViewModel", "Logout error: ${e.message}", e)
            }
        }
    }

}