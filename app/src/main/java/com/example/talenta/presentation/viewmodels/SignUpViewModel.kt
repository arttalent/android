package com.example.talenta.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.Artist
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed class AuthUiStatee {
    object Idle : AuthUiStatee()
    object Loading : AuthUiStatee()
    object Success : AuthUiStatee()
    object OtpSent : AuthUiStatee()
    data class Error(val message: String) : AuthUiStatee()
}

data class SignUpData(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val countryCode: String = "",
    val phoneNumber: String = ""
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiStatee>(AuthUiStatee.Idle)
    val uiState: StateFlow<AuthUiStatee> = _uiState.asStateFlow()

    fun validateSignUpData(signUpData: SignUpData): String? {
        return when {
            signUpData.firstName.length < 2 -> "First name must be at least 2 characters"
            signUpData.lastName.length < 2 -> "Last name must be at least 2 characters"
            !isValidEmail(signUpData.email) -> "Invalid email address"
            signUpData.password.length < 8 -> "Password must be at least 8 characters"
            signUpData.phoneNumber.isEmpty() -> "Phone number is required"
            !isValidPhoneNumber(signUpData.phoneNumber) -> "Invalid phone number"
            else -> null
        }
    }

    fun startSignUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        countryCode: String,
        phoneNumber: String
    ) {
        val signUpData = SignUpData(
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            email = email.trim(),
            password = password,
            countryCode = countryCode,
            phoneNumber = phoneNumber.trim()
        )

        validateSignUpData(signUpData)?.let { error ->
            _uiState.value = AuthUiStatee.Error(error)
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = AuthUiStatee.Loading

                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid ?: throw Exception("Failed to create user")

                val formattedPhoneNumber = formatPhoneNumber(countryCode, phoneNumber)

                val artist = Artist(
                    id = userId,
                    firstName = firstName,
                    lastName = lastName,
                    mobileNumber = formattedPhoneNumber,
                    countryCode = countryCode,
                    email = email.trim(),
                    password = password.trim(),
                )

                firestore.collection("artists")
                    .document(userId)
                    .set(artist)
                    .await()

                // Only set success if Firestore operation is successful
                _uiState.value = AuthUiStatee.Success
            } catch (e: Exception) {
                _uiState.value = AuthUiStatee.Error(e.message ?: "Sign up failed")
            }
        }
    }


    private fun formatPhoneNumber(countryCode: String, phoneNumber: String): String {
        val cleanCountryCode = countryCode.replace("+", "").trim()
        val cleanPhoneNumber = phoneNumber.replace(Regex("[^0-9]"), "")
        return "+$cleanCountryCode$cleanPhoneNumber"
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val digits = phoneNumber.replace(Regex("[^0-9]"), "")
        return digits.length >= 10
    }
}