package com.example.talenta.data.repository

import android.net.Uri
import com.example.talenta.data.model.User
import com.example.talenta.utils.FirestoreResult
import com.example.talenta.utils.HelperFunctions
import com.example.talenta.utils.safeFirebaseCall
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class EditProfileRepository @Inject constructor(
    @Named("users")
    private val userCollection: CollectionReference,
) {
    suspend fun fetchUserData(userId: String): User? = withContext(Dispatchers.IO) {
        try {
            val documentSnapshot = userCollection.document(userId).get().await()
            documentSnapshot.toObject<User>()
        } catch (e: Exception) {
            Timber.e(e, "Failed to fetch user data for ID: $userId")
            null
        }
    }

    suspend fun updateUserData(userId: String, updatedUser: User): FirestoreResult<Boolean> =
        withContext(Dispatchers.IO) {
            safeFirebaseCall {
                val existingUserSnapshot = userCollection.document(userId).get().await()
                val existingUser = existingUserSnapshot.toObject(User::class.java)

                if (existingUser != null) {
                    val mergedUser = HelperFunctions.mergeUser(existingUser, updatedUser)
                    userCollection.document(userId).set(mergedUser).await()
                    true
                } else {
                    false
                }
            }
        }


    suspend fun deleteCertificate(
        userId: String, certificateUrl: String
    ): FirestoreResult<Boolean> = withContext(Dispatchers.IO) {
        safeFirebaseCall {
            val storageRef = FirebaseStorage.getInstance().reference.child(
                "certificates/$userId/${
                    certificateUrl.substringAfterLast("/")
                }"
            )

            storageRef.delete().await()
            true
        }
    }


    suspend fun uploadCertificate(
        userId: String, certificateUri: Uri, certificateName: String
    ): FirestoreResult<String> = withContext(Dispatchers.IO) {
        safeFirebaseCall {
            val storageRef =
                FirebaseStorage.getInstance().reference.child("certificates/$userId/$certificateName")

            storageRef.putFile(certificateUri).await()
            val downloadUrl = storageRef.downloadUrl.await()
            downloadUrl.toString()
        }
    }


}