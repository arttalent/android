package com.example.talenta.presentation.state

import com.example.talenta.data.model.User

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Success(val user: User) :
        ProfileUiState()  // Ensure Artist class is used correctly

    data class Error(val message: String) : ProfileUiState()
}
