package com.example.talenta.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.Artist
import com.example.talenta.domain.AuthUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _verificationId = MutableStateFlow<String?>(null)
    private val _phoneNumber = MutableStateFlow<String>("")
    private val _userType = MutableStateFlow<String>("")
    private val _userData = MutableStateFlow<UserData?>(null)

    data class UserData(
        val name: String,
        val email: String,
        val phoneNumber: String
    )

    fun setUserType(type: String) {
        _userType.value = type
    }

    fun startSignUp(name: String, email: String, phoneNumber: String) {
        _userData.value = UserData(name, email, phoneNumber)
        _phoneNumber.value = phoneNumber
        sendOtp(phoneNumber)
    }

    fun sendOtp(phoneNumber: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            authUseCase.sendOtp(phoneNumber).fold(
                onSuccess = { verificationId ->
                    _verificationId.value = verificationId
                    _uiState.value = AuthUiState.OtpSent
                },
                onFailure = { e ->
                    _uiState.value = AuthUiState.Error(e.message ?: "Failed to send OTP")
                }
            )
        }
    }

    fun resendOtp() {
        _phoneNumber.value.let { phoneNumber ->
            if (phoneNumber.isNotEmpty()) {
                sendOtp(phoneNumber)
            }
        }
    }

    fun verifyOtp(code: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val verificationId = _verificationId.value ?: run {
                _uiState.value = AuthUiState.Error("Verification ID not found")
                return@launch
            }

            authUseCase.verifyOtp(verificationId, code).fold(
                onSuccess = { user ->
                    // Create initial profile after successful verification
                    _userData.value?.let { userData ->
                        createInitialProfile(user.uid, userData)
                    } ?: run {
                        _uiState.value = AuthUiState.Success(user)
                    }
                },
                onFailure = { e ->
                    _uiState.value = AuthUiState.Error(e.message ?: "Failed to verify OTP")
                }
            )
        }
    }

    private fun createInitialProfile(userId: String, userData: UserData) {
        viewModelScope.launch {
            val initialArtist = Artist(
                id = userId,
                firstName = userData.name.split(" ").firstOrNull() ?: "",
                lastName = userData.name.split(" ").drop(1).joinToString(" "),
                mobileNumber = userData.phoneNumber,
                profession = _userType.value
            )

            authUseCase.createArtistProfile(initialArtist, null).fold(
                onSuccess = {
                    _uiState.value =
                        AuthUiState.Success(firebaseAuth.currentUser!!) // Use injected firebaseAuth
                },
                onFailure = { e ->
                    _uiState.value = AuthUiState.Error(e.message ?: "Failed to create profile")
                }
            )
        }
    }
}

sealed class AuthUiState {
    object Initial : AuthUiState()
    object Loading : AuthUiState()
    object OtpSent : AuthUiState()
    data class Success(val user: FirebaseUser) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
