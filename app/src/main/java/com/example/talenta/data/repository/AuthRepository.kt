package com.example.talenta.data.repository

import android.net.Uri
import com.example.talenta.data.model.Artist
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
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
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    private val artistsCollection = firestore.collection("artists")

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
                val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

                val photoUrl = imageUri?.let { uri ->
                    val photoRef = storage.reference.child("profile_photos/$userId.jpg")
                    photoRef.putFile(uri).await()
                    photoRef.downloadUrl.await().toString()
                }

                val artistData = artist.copy(
                    id = userId,
                    photoUrl = photoUrl ?: artist.photoUrl
                )

                artistsCollection.document(userId).set(artistData).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
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
}