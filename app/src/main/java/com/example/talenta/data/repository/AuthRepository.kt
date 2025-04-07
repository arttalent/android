package com.example.talenta.data.repository

import android.net.Uri
import com.example.talenta.data.UserPreferences
import com.example.talenta.data.model.Artist
import com.example.talenta.data.model.Expert
import com.example.talenta.data.model.Role
import com.example.talenta.data.model.User
import com.example.talenta.presentation.state.AuthUiState
import com.example.talenta.utils.FirestoreResult
import com.example.talenta.utils.safeFirebaseCall
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.CollectionReference
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
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    @Named("users")
    private val userCollection: CollectionReference,
    private val preferences: UserPreferences,
) {
    val TAG = "AuthRepository"

    suspend fun signInWithEmail(email: String, password: String): AuthUiState {
        return try {
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
        role: Role
    ): FirestoreResult<Any> = withContext(Dispatchers.IO) {
        safeFirebaseCall {
            val authResult =
                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                    Timber.tag(TAG).d("User Created")
                    FirebaseAuth.getInstance().signOut()
                }.await()
            val userId = authResult.user?.uid ?: throw Exception("Failed to create user")

            val formattedPhoneNumber = formatPhoneNumber(countryCode, phoneNumber)

            val user = User(
                id = userId,
                firstName = firstName,
                lastName = lastName,
                email = email,
                phoneNumber = formattedPhoneNumber,
                role = role
            )
            userCollection.document(userId).set(user).await()
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


    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun logout() {
        auth.signOut()
    }

    suspend fun getUserProfile(): Result<User> = withContext(Dispatchers.IO) {
        try {
            val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
            val document = userCollection.document(userId).get().await()
            val user = document.toObject<User>()
            if (user == null) {
                Timber.tag(TAG).d("User not found")
                return@withContext Result.failure(IllegalStateException("User not found"))
            }
            preferences.saveUserData(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}