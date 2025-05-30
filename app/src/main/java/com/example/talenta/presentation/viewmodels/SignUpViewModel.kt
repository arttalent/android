package com.example.talenta.presentation.viewmodels


import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.UserPreferences
import com.example.talenta.data.model.ProfessionalData
import com.example.talenta.data.model.Role
import com.example.talenta.data.model.User
import com.example.talenta.data.repository.AuthRepository
import com.example.talenta.utils.FirestoreResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val countryCode: String = "+91",
    val phoneNumber: String = "",
    val selectedRole: Role? = null,
    val profession: String = ""
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
        val selectedRole: Role? = null,
        val profession: String? = null
    ) : AuthUiActions()
}

sealed class SignUpEvents {
    object SignUpSuccess : SignUpEvents()
    data class Error(val message: String) : SignUpEvents()
    object None : SignUpEvents()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val authRepository: AuthRepository,
    private val preferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiStates())
    val uiState: StateFlow<SignUpUiStates> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SignUpEvents>(
        replay = 0, extraBufferCapacity = 1
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
                        selectedRole = action.selectedRole ?: currentState.selectedRole,
                        profession = action.profession ?: currentState.profession
                    )
                }
            }

            AuthUiActions.SignUp -> startSignUp()
        }
    }

    private fun startSignUp() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val formattedPhone = formatPhoneNumber(
                _uiState.value.countryCode, _uiState.value.phoneNumber
            )

            // Create ProfessionalData with the selected profession
            val professionalData = ProfessionalData(
                profession = _uiState.value.profession,
                subProfession = "", // Can be added later or set based on profession
                media = emptyList(),
                skills = emptyList(),
                certifications = emptyList(),
                certificatesList = emptyList()
            )

            val user = User(
                id = auth.currentUser?.uid ?: "",
                firstName = _uiState.value.firstName.trim(),
                lastName = _uiState.value.lastName.trim(),
                email = _uiState.value.email.trim(),
                phoneNumber = formattedPhone,
                role = _uiState.value.selectedRole ?: Role.ARTIST,
                professionalData = professionalData
            )

            val selectedRole = (_uiState.value.selectedRole ?: Role.ARTIST).toString()
            viewModelScope.launch {
                preferences.saveUserRole(selectedRole)
            }

            val password = _uiState.value.password
            val confirmPassword = _uiState.value.confirmPassword

            val validationError = validateUser(user, confirmPassword, password)
            if (validationError != null) {
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(SignUpEvents.Error(validationError))
                return@launch
            }

            when (val result = authRepository.startSignUp(user, password)) {
                is FirestoreResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    result.errorMessage?.let { SignUpEvents.Error(it) }?.let { _events.emit(it) }
                }

                is FirestoreResult.Success<*> -> {
                    _uiState.update {
                        it.copy(successMessage = "Sign-up successful", isLoading = false)
                    }
                    _events.emit(SignUpEvents.SignUpSuccess)
                }
            }
        }
    }

    private fun formatPhoneNumber(countryCode: String, phoneNumber: String): String {
        val cleanCountryCode = countryCode.replace("+", "").trim()
        val cleanPhoneNumber = phoneNumber.replace(Regex("[^0-9]"), "")
        return "+$cleanCountryCode$cleanPhoneNumber"
    }

    private fun validateUser(user: User, confirmPassword: String, password: String): String? {
        if (user.firstName.isBlank()) return "First name is required."
        if (user.lastName.isBlank()) return "Last name is required."
        if (user.email.isBlank()) return "Email is required."
        if (!Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) return "Invalid email address."
        if (password.length < 6) return "Password must be at least 6 characters."
        if (password != confirmPassword) return "Passwords do not match."
        if (user.phoneNumber.isBlank()) return "Phone number is required."
        if (user.role == null) return "Role must be selected."
        if (user.professionalData.profession.isBlank()) return "Profession must be selected."

        return null
    }
}