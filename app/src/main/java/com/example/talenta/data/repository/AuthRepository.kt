package com.example.talenta.data.repository

import android.net.Uri
import android.util.Log
import com.example.talenta.data.model.Artist
import com.example.talenta.data.model.Expert
import com.example.talenta.data.model.Person
import com.example.talenta.presentation.state.AuthUiState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    val TAG = "AuthRepository"
    private val artistsCollection = firestore.collection("artists")


    private val expertsCollection = firestore.collection("experts")

    suspend fun uploadExpert(expert: Expert): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            println("uploading $expert")
            val expertData = hashMapOf(
                "id" to expert.id,
                "firstName" to expert.person.firstName,
                "lastName" to expert.person.lastName,
                "email" to expert.person.email,
                "profession" to expert.person.profession,
                "subProfession" to expert.person.subProfession,
                "countryCode" to expert.person.countryCode,
                "mobileNumber" to expert.person.mobileNumber,
                "photoUrl" to expert.person.photoUrl,
                "gender" to expert.person.gender,
                "age" to expert.person.age,
                "birthYear" to expert.person.birthYear,
                "language" to expert.person.language,
                "height" to expert.person.height,
                "weight" to expert.person.weight,
                "ethnicity" to expert.person.ethnicity,
                "color" to expert.person.color,
                "city" to expert.person.city,
                "country" to expert.person.country,
                "bioData" to expert.person.bioData,
                "socialMediaLinks" to expert.person.socialMediaLinks,
                "certificatesList" to expert.person.certificatesList,
                "photos" to expert.person.photos,
                "videos" to expert.person.videos,
                "skills" to expert.person.skills,
                "reviews" to expert.reviews,
                "location" to expert.location,
                "rating" to expert.rating,
                "followers" to expert.followers
            )

            expertsCollection.document(expert.id).set(expertData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            println(e)

            Result.failure(e)
        }
    }


    suspend fun signInWithEmail(email: String, password: String): AuthUiState {
        return try {
            // Check if user exists in Firestore
            val isUserRegistered = checkIfUserExists(email)
            if (!isUserRegistered) {
                return AuthUiState.Error("Email not registered. Please sign up.")
            }

            // Attempt authentication
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                AuthUiState.Success
            } else {
                AuthUiState.Error("Authentication failed. Please try again.")
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is FirebaseAuthInvalidUserException -> "No account found with this email."
                is FirebaseAuthInvalidCredentialsException -> "Invalid email or password."
                else -> "Authentication failed. Please try again."
            }
            Timber.tag("AuthRepository").e(e, "Sign in failed")
            AuthUiState.Error(errorMessage)
        }
    }

    suspend fun startSignUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        countryCode: String,
        phoneNumber: String,
        role: String  // Add role parameter
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                Timber.tag(TAG).d("User Created")
                FirebaseAuth.getInstance().signOut()
            }.addOnFailureListener {
                FirebaseAuth.getInstance().signOut()
                Timber.tag(TAG).d("Error Listner = %s", it.message)
            }.await()
            val userId = authResult.user?.uid ?: throw Exception("Failed to create user")

            val formattedPhoneNumber = formatPhoneNumber(countryCode, phoneNumber)

            when (role) {
                "Artist" -> {
                    val artist = Artist(
                        id = userId, person = Person(
                            firstName, lastName, formattedPhoneNumber, countryCode, email, password
                        )
                    )
                    artistsCollection.document(userId).set(artist).addOnSuccessListener {
                        Result.success(Unit)
                        Timber.tag(TAG).d("Successful Creation")
                    }
                        .addOnFailureListener { e ->
                            Timber.tag(TAG).d("Error Listner = %s", e.message)
                        }.await()
                }

                "Expert" -> {
                    val expert = Expert(
                        id = userId, person = Person(
                            firstName, lastName, formattedPhoneNumber, countryCode, email, password
                        )
                    )
                    expertsCollection.document(userId).set(expert).await()
                }

                "Member" -> {

                    // yet to implemented
                }

                "Sponsor" -> {
                    // yet to implemented

                }

                else -> throw Exception("Invalid role selected")
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Timber.tag(TAG).d("Error = %s", e.message)
            Result.failure(e)
        }
    }


    private fun formatPhoneNumber(countryCode: String, phoneNumber: String): String {
        val cleanCountryCode = countryCode.replace("+", "").trim()
        val cleanPhoneNumber = phoneNumber.replace(Regex("[^0-9]"), "")
        return "+$cleanCountryCode$cleanPhoneNumber"
    }


    suspend fun sendOtp(phoneNumber: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val verificationId = CompletableDeferred<String>()

            auth.setLanguageCode("en")
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

                override fun onVerificationFailed(e: FirebaseException) {
                    verificationId.completeExceptionally(e)
                }

                override fun onCodeSent(
                    vId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    verificationId.complete(vId)
                }
            }

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
            Result.success(verificationId.await())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyOtp(verificationId: String, code: String): Result<FirebaseUser> =
        withContext(Dispatchers.IO) {
            try {
                val credential = PhoneAuthProvider.getCredential(verificationId, code)
                val result = auth.signInWithCredential(credential).await()


                Result.success(result.user!!)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun createArtistProfile(artist: Artist, imageUri: Uri?): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val userId =
                    auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

                val photoUrl = imageUri?.let { uri ->
                    val photoRef = storage.reference.child("profile_photos/$userId.jpg")
                    photoRef.putFile(uri).await()
                    photoRef.downloadUrl.await().toString()
                }

                val updatedArtist = artist.copy(
                    id = userId,
                    person = artist.person.copy(photoUrl = photoUrl ?: artist.person.photoUrl)
                )

                artistsCollection.document(userId).set(updatedArtist).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }


    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun logout() {
        auth.signOut()
    }

    suspend fun fetchArtistProfile(userId: String): Artist? {
        val artistDoc = firestore.collection("artists").document(userId).get().await()
        return if (artistDoc.exists()) {
            artistDoc.toObject(Artist::class.java)?.copy(id = userId)
        } else {
            null
        }

    }

    suspend fun getArtistProfile(): Result<Artist> = withContext(Dispatchers.IO) {
        try {
            val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
            val document = artistsCollection.document(userId).get().await()
            val artist = document.toObject<Artist>()
            Result.success(artist ?: throw IllegalStateException("Artist profile not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private suspend fun checkIfUserExists(email: String): Boolean {
        return try {
            val querySnapshot =
                firestore.collection("artists").whereEqualTo("email", email).get().await()

            !querySnapshot.isEmpty
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error checking user existence", e)
            true
        }
    }
}