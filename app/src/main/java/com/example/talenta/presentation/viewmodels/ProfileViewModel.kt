package com.example.talenta.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.Artist
import com.example.talenta.domain.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _artist = MutableStateFlow<Artist?>(null)
    val artist = _artist.asStateFlow()

    fun updateProfile(artist: Artist, imageUri: Uri?) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            authUseCase.createArtistProfile(artist, imageUri).fold(
                onSuccess = {
                    _artist.value = artist
                    _uiState.value = ProfileUiState.Success
                },
                onFailure = { e ->
                    _uiState.value = ProfileUiState.Error(e.message ?: "Failed to update profile")
                }
            )
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            authUseCase.getArtistProfile().fold(
                onSuccess = { artist ->
                    _artist.value = artist
                    _uiState.value = ProfileUiState.Success
                },
                onFailure = { e ->
                    _uiState.value = ProfileUiState.Error(e.message ?: "Failed to load profile")
                }
            )
        }
    }
}
sealed class ProfileUiState {
    object Initial : ProfileUiState()
    object Loading : ProfileUiState()
    object Success : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}