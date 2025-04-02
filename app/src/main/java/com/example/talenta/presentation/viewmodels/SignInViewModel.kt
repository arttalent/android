package com.example.talenta.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.repository.AuthRepository
import com.example.talenta.presentation.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            _uiState.value = authRepository.signInWithEmail(email, password)
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
