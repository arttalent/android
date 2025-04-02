package com.example.talenta.presentation.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.Artist
import com.example.talenta.data.model.Certificate
import com.example.talenta.data.model.Person
import com.example.talenta.data.model.SocialMediaLinks
import com.example.talenta.presentation.state.EditProfileEvent
import com.example.talenta.presentation.state.EditProfileState
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

                val artistDoc = firestore.collection("artists").document(userId).get().await()

                val artist = artistDoc.toObject(Artist::class.java) ?: Artist(
                    id = userId,
                    person = Person()
                )

                // Ensure certificates are properly handled
                val certificatesList = if (artist.person.certificatesList.isNotEmpty()) {
                    artist.person.certificatesList
                } else {
                    val certificatesField = artistDoc.get("person.certificates")
                    when (certificatesField) {
                        is List<*> -> {
                            certificatesField.mapNotNull { item ->
                                (item as? Map<*, *>)?.let {
                                    Certificate(
                                        id = (it["id"] as? String) ?: UUID.randomUUID().toString(),
                                        imageUrl = (it["imageUrl"] as? String) ?: "",
                                        description = (it["description"] as? String) ?: "",
                                        timestamp = (it["timestamp"] as? Long)
                                            ?: System.currentTimeMillis()
                                    )
                                }
                            }
                        }
                        is Map<*, *> -> {
                            listOf(
                                Certificate(
                                    id = (certificatesField["id"] as? String) ?: UUID.randomUUID()
                                        .toString(),
                                    imageUrl = (certificatesField["imageUrl"] as? String) ?: "",
                                    description = (certificatesField["description"] as? String)
                                        ?: "",
                                    timestamp = (certificatesField["timestamp"] as? Long)
                                        ?: System.currentTimeMillis()
                                )
                            )
                        }
                        else -> emptyList()
                    }
                }

                _state.value = EditProfileState.Success(
                    firstName = artist.person.firstName,
                    lastName = artist.person.lastName,
                    email = artist.person.email,
                    profession = artist.person.profession,
                    city = artist.person.city,
                    country = artist.person.country,
                    gender = artist.person.gender,
                    age = artist.person.age.toString(),
                    birthYear = artist.person.birthYear.toString(),
                    language = artist.person.language,
                    height = artist.person.height,
                    weight = artist.person.weight,
                    bioData = artist.person.bioData,
                    facebook = artist.person.socialMediaLinks.facebook,
                    instagram = artist.person.socialMediaLinks.instagram,
                    linkedin = artist.person.socialMediaLinks.linkedin,
                    twitter = artist.person.socialMediaLinks.twitter,
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
                    Person(
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