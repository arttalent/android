package com.example.talenta.data.repository

import android.net.Uri
import com.example.talenta.data.Utilities
import com.example.talenta.data.model.Photo
import com.example.talenta.data.model.User
import com.example.talenta.data.model.Video
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ArtistRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val utilities: Utilities
) {


    suspend fun fetchArtistProfile(userId: String): User? = withContext(Dispatchers.IO) {
        try {
            val artistDoc = firestore.collection("users").document(userId).get().await()

            if (artistDoc.exists()) {
                try {
                    val user = artistDoc.toObject(User::class.java)
                    if (user != null) {
                        return@withContext user.copy(id = userId)
                    }
                } catch (_: Exception) {
                }
            }
            null
        } catch (e: Exception) {
            null
        }
    }


    suspend fun uploadMedia(imageUri: Uri, description: String, isVideo: Boolean): String {
        val userId = utilities.getCurrentUserId()
        val mediaType = if (isVideo) "videos" else "photos"
        val fileId = UUID.randomUUID().toString()
        val fileRef = storage.reference.child("$mediaType/$userId/$fileId")

        fileRef.putFile(imageUri).await()
        val downloadUrl = fileRef.downloadUrl.await().toString()

        val mediaData = if (isVideo) {
            mapOf(
                "id" to fileId,
                "videoUrl" to downloadUrl,
                "description" to description,
                "timestamp" to System.currentTimeMillis()
            )
        } else {
            mapOf(
                "id" to fileId,
                "imageUrl" to downloadUrl,
                "description" to description,
                "timestamp" to System.currentTimeMillis()
            )
        }

        // Save media in Firestore as a Map
        firestore.collection("users").document(userId).collection(mediaType).document(fileId)
            .set(mediaData).await()

        return downloadUrl
    }



    suspend fun updateProfilePhoto(downloadUrl: String, callback: (Exception?) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        try {
            firestore.collection("users").document(userId).update("profilePicture", downloadUrl)
                .await()
            callback(null) // Success, no error

        } catch (e: Exception) {
            callback(e)
        }
    }


    suspend fun fetchPhotos(userId: String): List<Photo> {
        return firestore.collection("users").document(userId).collection("photos").get()
            .await().documents.mapNotNull { it.toObject(Photo::class.java)?.copy(id = it.id) }
    }

    suspend fun fetchVideos(userId: String): List<Video> {
        return firestore.collection("users").document(userId).collection("videos").get()
            .await().documents.mapNotNull { it.toObject(Video::class.java)?.copy(id = it.id) }
    }


    suspend fun deleteMedia(mediaId: String, isVideo: Boolean) {
        try {
            val userId = utilities.getCurrentUserId()
            val mediaType = if (isVideo) "videos" else "photos"

            // Fetch media document to get file URL
            val mediaDoc = firestore.collection("users").document(userId).collection(mediaType)
                .document(mediaId).get().await()

            if (mediaDoc.exists()) {
                val fileUrl = if (isVideo) {
                    mediaDoc.getString("videoUrl")
                } else {
                    mediaDoc.getString("imageUrl")
                }

                // Delete from Firestore
                firestore.collection("users").document(userId).collection(mediaType)
                    .document(mediaId).delete().await()

                // Delete from Storage
                fileUrl?.let {
                    try {
                        val storageRef = storage.getReferenceFromUrl(it)
                        storageRef.delete().await()
                    } catch (e: Exception) {
                        // Log error if needed
                    }
                }
            }
        } catch (e: Exception) {
            throw Exception("Failed to delete media: ${e.message}")
        }
    }
}
