package com.example.talenta.presentation.state

import com.example.talenta.data.model.Artist

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Success(val artist: Artist) :
        ProfileUiState()  // Ensure Artist class is used correctly

    data class Error(val message: String) : ProfileUiState()
}
