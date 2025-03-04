package com.example.talenta.presentation.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.Artist
import com.example.talenta.data.model.Certificate
import com.example.talenta.data.model.SocialMediaLinks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

sealed class EditProfileState {
    object Loading : EditProfileState()
    data class Success(
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val profession: String = "",
        val city: String = "",
        val country: String = "",
        val gender: String = "",
        val age: String = "",
        val birthYear: String = "",
        val language: String = "",
        val height: String = "",
        val weight: String = "",
        val bioData: String = "",
        val facebook: String = "",
        val instagram: String = "",
        val linkedin: String = "",
        val twitter: String = "",
        val certificates: List<Certificate> = emptyList()
    ) : EditProfileState()

    data class Error(val message: String) : EditProfileState()
}

sealed class EditProfileEvent {
    object NavigateBack : EditProfileEvent()
    data class ShowError(val message: String) : EditProfileEvent()
    object ShowSuccessMessage : EditProfileEvent()
    // New event specifically for certificate operations that shouldn't navigate
    object CertificateOperationSuccess : EditProfileEvent()
}


// EditProfileViewModel Updates
class EditProfileViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow<EditProfileState>(EditProfileState.Loading)
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    private val _event = MutableStateFlow<EditProfileEvent?>(null)
    val event: StateFlow<EditProfileEvent?> = _event.asStateFlow()

    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    private val storage = FirebaseStorage.getInstance()

    private fun uploadCertificateImage(imageUri: Uri): Flow<String> = flow {
        val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
        val filename = "certificates/${userId}/${System.currentTimeMillis()}_${UUID.randomUUID()}"
        val imageRef = storage.reference.child(filename)

        imageRef.putFile(imageUri).await()
        val downloadUrl = imageRef.downloadUrl.await().toString()
        emit(downloadUrl)
    }

    init {
        fetchArtistData()
    }

    private fun fetchArtistData() {
        viewModelScope.launch {
            try {
                _state.value = EditProfileState.Loading
                val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")

                val artistDoc = firestore.collection("artists")
                    .document(userId)
                    .get()
                    .await()

                val artist = artistDoc.toObject(Artist::class.java) ?: Artist(id = userId)

                // Get certificates from the new field or create from the old field if needed
                val certificatesList = if (artist.certificatesList.isNotEmpty()) {
                    artist.certificatesList
                } else {
                    // Read certificates directly from Firestore as a fallback
                    val certificatesField = artistDoc.get("certificates")
                    when (certificatesField) {
                        // If it's a list, convert to Certificate objects
                        is List<*> -> {
                            certificatesField.mapNotNull { item ->
                                if (item is Map<*, *>) {
                                    try {
                                        Certificate(
                                            id = (item["id"] as? String) ?: UUID.randomUUID()
                                                .toString(),
                                            imageUrl = (item["imageUrl"] as? String) ?: "",
                                            description = (item["description"] as? String) ?: "",
                                            timestamp = (item["timestamp"] as? Long)
                                                ?: System.currentTimeMillis()
                                        )
                                    } catch (e: Exception) {
                                        null
                                    }
                                } else null
                            }
                        }
                        // If it's a single object, convert to a list with one item
                        is Map<*, *> -> {
                            try {
                                listOf(
                                    Certificate(
                                        id = (certificatesField["id"] as? String)
                                            ?: UUID.randomUUID().toString(),
                                        imageUrl = (certificatesField["imageUrl"] as? String) ?: "",
                                        description = (certificatesField["description"] as? String)
                                            ?: "",
                                        timestamp = (certificatesField["timestamp"] as? Long)
                                            ?: System.currentTimeMillis()
                                    )
                                )
                            } catch (e: Exception) {
                                emptyList()
                            }
                        }

                        else -> emptyList()
                    }
                }

                _state.value = EditProfileState.Success(
                    firstName = artist.firstName,
                    lastName = artist.lastName,
                    email = artist.email,
                    profession = artist.profession,
                    city = artist.city,
                    country = artist.country,
                    gender = artist.gender,
                    age = artist.age.toString(),
                    birthYear = artist.birthYear.toString(),
                    language = artist.language,
                    height = artist.height,
                    weight = artist.weight,
                    bioData = artist.bioData,
                    facebook = artist.socialMediaLinks.facebook,
                    instagram = artist.socialMediaLinks.instagram,
                    linkedin = artist.socialMediaLinks.linkedin,
                    twitter = artist.socialMediaLinks.twitter,
                    certificates = certificatesList
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

                val imageUrl = uploadCertificateImage(imageUri).first()
                val newCertificate = Certificate(
                    id = UUID.randomUUID().toString(),
                    imageUrl = imageUrl,
                    description = description,
                    timestamp = System.currentTimeMillis()
                )

                val updatedCertificates = currentState.certificates + newCertificate

                // Update Firestore
                val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
                firestore.collection("artists")
                    .document(userId)
                    .update("certificatesList", updatedCertificates)
                    .await()

                // Update local state with new certificates list
                _state.value = currentState.copy(certificates = updatedCertificates)

                // Use the new certificate-specific event instead of ShowSuccessMessage
                _event.value = EditProfileEvent.CertificateOperationSuccess
            } catch (e: Exception) {
                _event.value = EditProfileEvent.ShowError(e.message ?: "Failed to add certificate")
            }
        }
    }

    // Do the same for deleteCertificate:
    fun deleteCertificate(certificate: Certificate) {
        viewModelScope.launch {
            try {
                val currentState = _state.value as? EditProfileState.Success ?: return@launch

                // Delete from Storage
                if (certificate.imageUrl.isNotEmpty()) {
                    try {
                        val imageRef = storage.getReferenceFromUrl(certificate.imageUrl)
                        imageRef.delete().await()
                    } catch (e: Exception) {
                        Log.e("EditProfileViewModel", "Failed to delete image: ${e.message}")
                    }
                }

                val updatedCertificates = currentState.certificates.filter { it.id != certificate.id }

                // Update Firestore
                val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
                firestore.collection("artists")
                    .document(userId)
                    .update("certificatesList", updatedCertificates)
                    .await()

                // Update local state
                _state.value = currentState.copy(certificates = updatedCertificates)

                // Use certificate-specific event
                _event.value = EditProfileEvent.CertificateOperationSuccess
            } catch (e: Exception) {
                _event.value = EditProfileEvent.ShowError(e.message ?: "Failed to delete certificate")
            }
        }
    }


    // Rest of the ViewModel methods remain the same
    fun updateField(field: String, value: String) {
        val currentState = _state.value
        if (currentState is EditProfileState.Success) {
            _state.value = when (field) {
                "firstName" -> currentState.copy(firstName = value)
                "lastName" -> currentState.copy(lastName = value)
                "email" -> currentState.copy(email = value)
                "profession" -> currentState.copy(profession = value)
                "city" -> currentState.copy(city = value)
                "country" -> currentState.copy(country = value)
                "gender" -> currentState.copy(gender = value)
                "age" -> currentState.copy(age = value)
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
    }

    fun setCurrentStep(step: Int) {
        _currentStep.value = step
    }

    fun saveProfile() {
        viewModelScope.launch {
            try {
                val currentState = _state.value
                if (currentState !is EditProfileState.Success) {
                    _event.value = EditProfileEvent.ShowError("Invalid state")
                    return@launch
                }

                _state.value = EditProfileState.Loading
                val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")

                val updatedArtist = Artist(
                    id = userId,
                    firstName = currentState.firstName,
                    lastName = currentState.lastName,
                    email = currentState.email,
                    profession = currentState.profession,
                    city = currentState.city,
                    country = currentState.country,
                    gender = currentState.gender,
                    age = currentState.age.toIntOrNull() ?: 0,
                    birthYear = currentState.birthYear.toIntOrNull() ?: 0,
                    language = currentState.language,
                    height = currentState.height,
                    weight = currentState.weight,
                    bioData = currentState.bioData,
                    socialMediaLinks = SocialMediaLinks(
                        facebook = currentState.facebook,
                        instagram = currentState.instagram,
                        linkedin = currentState.linkedin,
                        twitter = currentState.twitter
                    ),
                    certificatesList = currentState.certificates
                )

                firestore.collection("artists")
                    .document(userId)
                    .set(updatedArtist)
                    .await()

                _event.value = EditProfileEvent.ShowSuccessMessage
                _state.value = currentState // Restore the previous success state
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