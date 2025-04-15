package com.example.talenta.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.Certificate
import com.example.talenta.data.repository.EditProfileRepository
import com.example.talenta.presentation.state.EditProfileEvent
import com.example.talenta.presentation.state.EditProfileState
import com.example.talenta.utils.FirestoreResult
import com.example.talenta.utils.HelperFunctions.merge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: EditProfileRepository
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow<EditProfileState>(EditProfileState.Loading)
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    private val _event = MutableStateFlow<EditProfileEvent?>(null)
    val event: StateFlow<EditProfileEvent?> = _event.asStateFlow()

    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    init {
        fetchArtistData()
    }

    private fun fetchArtistData() {
        viewModelScope.launch {
            try {
                _state.value = EditProfileState.Loading

                val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
                val user =
                    repository.fetchUserData(userId) ?: throw Exception("User data not found")

                val certificatesList = user.professionalData.certificatesList

                _state.value = EditProfileState.Success(
                    firstName = user.firstName,
                    lastName = user.lastName,
                    email = user.email,
                    phoneNumber = user.phoneNumber,
                    profilePicture = user.profilePicture,
                    role = user.role,
                    isVerified = user.isVerified,
                    blocked = user.blocked,
                    city = user.bio.city,
                    country = user.bio.country,
                    bioData = user.bio.bioData,
                    language = user.bio.language,
                    facebook = user.bio.socialMediaLinks.facebook,
                    instagram = user.bio.socialMediaLinks.instagram,
                    linkedin = user.bio.socialMediaLinks.linkedin,
                    twitter = user.bio.socialMediaLinks.twitter,
                    gender = user.physicalAttributes.gender,
                    age = user.physicalAttributes.age,
                    height = user.physicalAttributes.height,
                    weight = user.physicalAttributes.weight,
                    ethnicity = user.physicalAttributes.ethnicity,
                    color = user.physicalAttributes.color,
                    profession = user.professionalData.profession,
                    subProfession = user.professionalData.subProfession,
                    media = user.professionalData.media,
                    skills = user.professionalData.skills,
                    certifications = user.professionalData.certifications,
                    certificatesList = certificatesList,
                    birthYear = ""
                )
            } catch (e: Exception) {
                _state.value = EditProfileState.Error(e.message ?: "Unknown error occurred")
                _event.value = EditProfileEvent.ShowError(e.message ?: "Failed to load profile")
            }
        }
    }

    fun addCertificate(imageUri: Uri, description: String) {
        viewModelScope.launch {
            try {
                val currentState = _state.value as? EditProfileState.Success ?: return@launch
                val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
                val certificateId = UUID.randomUUID().toString()

                val uploadResult = repository.uploadCertificate(
                    userId = userId, certificateUri = imageUri, certificateName = certificateId
                )

                when (uploadResult) {
                    is FirestoreResult.Success -> {
                        val imageUrl = uploadResult.data
                        val newCertificate = Certificate(
                            id = certificateId,
                            imageUrl = imageUrl,
                            description = description,
                            timestamp = System.currentTimeMillis()
                        )

                        val updatedCertificates = currentState.certificatesList + newCertificate

                        firestore.collection("users").document(userId)
                            .update("professionalData.certificatesList", updatedCertificates)
                            .await()

                        _state.value = currentState.copy(certificatesList = updatedCertificates)
                        _event.value = EditProfileEvent.CertificateOperationSuccess
                    }

                    is FirestoreResult.Failure -> {
                        _event.value = EditProfileEvent.ShowError(uploadResult.errorMessage)
                    }
                }
            } catch (e: Exception) {
                _event.value = EditProfileEvent.ShowError(e.message ?: "Failed to add certificate")
            }
        }
    }

    fun deleteCertificate(certificate: Certificate) {
        viewModelScope.launch {
            try {
                val currentState = _state.value as? EditProfileState.Success ?: return@launch
                val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")

                repository.deleteCertificate(userId, certificate.imageUrl)

                val updatedCertificates = currentState.certificatesList.filter {
                    it.id != certificate.id
                }

                firestore.collection("users").document(userId)
                    .update("professionalData.certificatesList", updatedCertificates)
                    .await()

                _state.value = currentState.copy(certificatesList = updatedCertificates)
                _event.value = EditProfileEvent.CertificateOperationSuccess
            } catch (e: Exception) {
                _event.value = EditProfileEvent.ShowError(e.message ?: "Failed to delete certificate")
            }
        }
    }

    fun updateField(field: String, value: String) {
        val currentState = _state.value as? EditProfileState.Success ?: return

        _state.value = when (field) {
            "firstName" -> currentState.copy(firstName = value)
            "lastName" -> currentState.copy(lastName = value)
            "email" -> currentState.copy(email = value)
            "profession" -> currentState.copy(profession = value)
            "city" -> currentState.copy(city = value)
            "country" -> currentState.copy(country = value)
            "gender" -> currentState.copy(gender = value)
            "age" -> currentState.copy(age = value.toIntOrNull() ?: currentState.age)
            "birthYear" -> currentState.copy(birthYear = value)
            "language" -> currentState.copy(language = value)
            "height" -> currentState.copy(height = value)
            "weight" -> currentState.copy(weight = value)
            "bioData" -> currentState.copy(bioData = value)
            "facebook" -> currentState.copy(facebook = value)
            "instagram" -> currentState.copy(instagram = value)
            "linkedin" -> currentState.copy(linkedin = value)
            "twitter" -> currentState.copy(twitter = value)
            else -> currentState
        }
    }

    fun setCurrentStep(step: Int) {
        _currentStep.value = step
    }

    fun saveProfile() {
        viewModelScope.launch {
            try {
                val currentState =
                    _state.value as? EditProfileState.Success ?: throw Exception("Invalid state")

                _state.value = EditProfileState.Loading
                val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")

                val existingUser = repository.fetchUserData(userId)
                    ?: throw Exception("User not found")

                val updatedUser = existingUser.merge(currentState)

                repository.updateUserData(userId, updatedUser)

                _event.value = EditProfileEvent.ShowSuccessMessage
                _state.value = currentState
            } catch (e: Exception) {
                _state.value = EditProfileState.Error(e.message ?: "Unknown error occurred")
                _event.value = EditProfileEvent.ShowError(e.message ?: "Failed to save profile")
            }
        }
    }


    fun onEventHandled() {
        _event.value = null
    }
}
