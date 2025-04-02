package com.example.talenta.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.Expert
import com.example.talenta.data.model.Person
import com.example.talenta.data.model.Role
import com.example.talenta.data.model.SignUpData
import com.example.talenta.data.repository.AuthRepository
import com.example.talenta.presentation.state.AuthUiStatee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiStatee>(AuthUiStatee.Idle)
    val uiState: StateFlow<AuthUiStatee> = _uiState.asStateFlow()


    fun uploadExpert(expert: Expert) {
        val validationError = validateExpertData(expert.person)

        if (validationError != null) {
            _uiState.value = AuthUiStatee.Error(validationError)
            return
        }

        _uiState.value = AuthUiStatee.Loading

        viewModelScope.launch {
            val result = authRepository.uploadExpert(expert)

            _uiState.value = if (result.isSuccess) {
                AuthUiStatee.Success
            } else {
                AuthUiStatee.Error(result.exceptionOrNull()?.message ?: "Expert upload failed")
            }
        }
    }

    fun startSignUp(signUpData: SignUpData, role: Role) {
        val validationError = validateSignUpData(signUpData)

        if (validationError != null) {
            _uiState.value = AuthUiStatee.Error(validationError)
            return
        }

        _uiState.value = AuthUiStatee.Loading

        viewModelScope.launch {
            val result = authRepository.startSignUp(
                signUpData.firstName.trim(),
                signUpData.lastName.trim(),
                signUpData.email.trim(),
                signUpData.password,
                signUpData.countryCode.trim(),
                signUpData.phoneNumber.trim(),
                role = role

            )

            _uiState.value = if (result.isSuccess) {
                AuthUiStatee.Success
            } else {
                AuthUiStatee.Error(result.exceptionOrNull()?.message ?: "Sign-up failed")
            }
        }
    }

    private fun validateSignUpData(signUpData: SignUpData): String? {
        return when {
            signUpData.firstName.length < 2 -> "First name must be at least 2 characters"
            signUpData.lastName.length < 2 -> "Last name must be at least 2 characters"
            !isValidEmail(signUpData.email) -> "Invalid email address"
            signUpData.password.length < 8 -> "Password must be at least 8 characters"
            signUpData.countryCode.isBlank() -> "Country code is required"
            signUpData.phoneNumber.isBlank() -> "Phone number is required"
            !isValidPhoneNumber(signUpData.phoneNumber) -> "Invalid phone number"
            else -> null
        }
    }


    private fun validateExpertData(person: Person): String? {
        return when {
            person.firstName.length < 2 -> "First name must be at least 2 characters"
            person.lastName.length < 2 -> "Last name must be at least 2 characters"
            !isValidEmail(person.email) -> "Invalid email address"
            person.mobileNumber.isBlank() -> "Mobile number is required"
            !isValidPhoneNumber(person.mobileNumber) -> "Invalid mobile number"
            person.profession.isBlank() -> "Profession is required"
            person.city.isBlank() -> "City is required"
            person.country.isBlank() -> "Country is required"
            person.bioData.isBlank() -> "BioData is required"
            person.photoUrl.isBlank() -> "Profile image URL is required"
            else -> null
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val digits = phoneNumber.replace(Regex("[^0-9]"), "")
        return digits.length >= 10
    }
}
