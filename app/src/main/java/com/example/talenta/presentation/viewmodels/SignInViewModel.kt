package com.example.talenta.presentation.viewmodels


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    sealed class AuthUiState {
        data object Idle : AuthUiState()
        data object Loading : AuthUiState()
        data object Success : AuthUiState()
        data class Error(val message: String) : AuthUiState()
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = AuthUiState.Loading

                // Check if user exists in Firestore
                val isUserRegistered = checkIfUserExists(email)
                if (!isUserRegistered) {
                    _uiState.value = AuthUiState.Error("Email not registered. Please sign up.")
                    return@launch
                }

                // Attempt authentication
                val result = auth.signInWithEmailAndPassword(email, password).await()

                if (result.user != null) {
                    _uiState.value = AuthUiState.Success
                } else {
                    _uiState.value = AuthUiState.Error("Authentication failed. Please try again.")
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is FirebaseAuthInvalidUserException -> "No account found with this email."
                    is FirebaseAuthInvalidCredentialsException -> "Invalid email or password."
                    else -> "Authentication failed. Please try again."
                }
                Log.e("SignInViewModel", "Sign in failed", e)
                _uiState.value = AuthUiState.Error(errorMessage)
            }
        }
    }

    private suspend fun checkIfUserExists(email: String): Boolean {
        return try {
            val querySnapshot = firestore.collection("artists")
                .whereEqualTo("email", email)
                .get()
                .await()

            !querySnapshot.isEmpty
        } catch (e: Exception) {
            Log.e("SignInViewModel", "Error checking user existence", e)
            true
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}