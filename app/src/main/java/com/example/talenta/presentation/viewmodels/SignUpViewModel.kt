package com.example.talenta.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.Person
import com.example.talenta.data.model.Role
import com.example.talenta.data.model.SignUpData
import com.example.talenta.data.repository.AuthRepository
import com.example.talenta.utils.FirestoreResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignUpUiStates(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val countryCode: String = "",
    val phoneNumber: String = "",
    val selectedRole: Role? = null,
)

sealed class AuthUiActions {
    object SignUp : AuthUiActions()
    data class UpdateData(
        val firstName: String? = null,
        val lastName: String? = null,
        val email: String? = null,
        val password: String? = null,
        val confirmPassword: String? = null,
        val countryCode: String? = null,
        val phoneNumber: String? = null,
        val selectedRole: Role? = null
    ) : AuthUiActions()
}

sealed class SignUpEvents {
    object SignUpSuccess : SignUpEvents()
    data class Error(val message: String) : SignUpEvents()
    object None : SignUpEvents()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpUiStates>(SignUpUiStates())
    val uiState: StateFlow<SignUpUiStates> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SignUpEvents>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val events = _events.asSharedFlow()

    fun handleAction(action: AuthUiActions) {
        when (action) {
            is AuthUiActions.UpdateData -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        firstName = action.firstName ?: currentState.firstName,
                        lastName = action.lastName ?: currentState.lastName,
                        email = action.email ?: currentState.email,
                        password = action.password ?: currentState.password,
                        confirmPassword = action.confirmPassword ?: currentState.confirmPassword,
                        countryCode = action.countryCode ?: currentState.countryCode,
                        phoneNumber = action.phoneNumber ?: currentState.phoneNumber,
                        selectedRole = action.selectedRole ?: currentState.selectedRole
                    )
                }
            }

            AuthUiActions.SignUp -> startSignUp()
        }
    }

    fun startSignUp() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val signUpData = SignUpData(
                firstName = _uiState.value.firstName,
                lastName = _uiState.value.lastName,
                email = _uiState.value.email,
                password = _uiState.value.password,
                countryCode = _uiState.value.countryCode,
                confirmPassword = _uiState.value.confirmPassword,
                phoneNumber = _uiState.value.phoneNumber
            )
            val role = _uiState.value.selectedRole
            val validationError = validateSignUpData(signUpData)

            if (validationError != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
                _events.emit(SignUpEvents.Error(validationError))
                return@launch
            }

            role?.let {
                val result = authRepository.startSignUp(
                    signUpData.firstName.trim(),
                    signUpData.lastName.trim(),
                    signUpData.email.trim(),
                    signUpData.password,
                    signUpData.countryCode.trim(),
                    signUpData.phoneNumber.trim(),
                    role = role

                )
                when (result) {
                    is FirestoreResult.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                        _events.emit(SignUpEvents.Error(result.errorMessage))
                    }

                    is FirestoreResult.Success<*> -> {
                        _uiState.update {
                            it.copy(
                                successMessage = "Sign-up successful",
                                isLoading = false
                            )
                        }
                        _events.emit(SignUpEvents.SignUpSuccess)
                    }
                }
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
            signUpData.confirmPassword.trim() != signUpData.password.trim() -> "Passwords do not match"
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
