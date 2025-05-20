package com.example.talenta.data.repository

import com.example.talenta.data.UserPreferences
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
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.google.firebase.messaging.FirebaseMessaging
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
    @Named("users") private val userCollection: CollectionReference,
    private val preferences: UserPreferences,
) {
    private val TAG = "AuthRepository"
    val firebaseMessaging = FirebaseMessaging.getInstance()

    suspend fun signInWithEmail(email: String, password: String): AuthUiState {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                updateFirebaseToken(firebaseMessaging.token.await())
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


    suspend fun updateFirebaseToken(token: String): FirestoreResult<Unit> {
        return safeFirebaseCall {
            val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
            userCollection.document(userId).update("firebaseToken", token).await()
            FirestoreResult.Success(Unit)
        }
    }


    suspend fun startSignUp(
        user: User, password: String
    ): FirestoreResult<Unit> = withContext(Dispatchers.IO) {
        safeFirebaseCall {
            auth.createUserWithEmailAndPassword(user.email, password).await()
            val userId = FirebaseAuth.getInstance().currentUser?.uid
                ?: throw Exception("Failed to create user")

            Timber.tag(TAG).d("User Created with ID: $userId")

            userCollection.document(userId).set(user.copy(id = userId), SetOptions.merge()).await()
            FirebaseAuth.getInstance().signOut()
        }
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
                    vId: String, token: PhoneAuthProvider.ForceResendingToken
                ) {
                    verificationId.complete(vId)
                }
            }

            val options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS).setCallbacks(callbacks).build()

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