package com.example.talenta.data.repository

import android.net.Uri
import com.example.talenta.data.Utilities
import com.example.talenta.data.model.Artist
import com.example.talenta.data.model.Photo
import com.example.talenta.data.model.Video
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Singleton


@Singleton
class ArtistRepository(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val utilities: Utilities
) {


    suspend fun fetchArtistProfile(userId: String): Artist? {
        val artistDoc = firestore.collection("artists").document(userId).get().await()
        return if (artistDoc.exists()) {
            artistDoc.toObject(Artist::class.java)?.copy(id = userId)
        } else {
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
        firestore.collection("artists").document(userId).collection(mediaType).document(fileId)
            .set(mediaData).await()

        return downloadUrl
    }


    suspend fun updateProfilePhoto(downloadUrl: String, callback: (Exception?) -> Unit) {
        val userId = utilities.getCurrentUserId()

        try {
            firestore.collection("artists").document(userId).update("photoUrl", downloadUrl).await()
            callback(null) // Success, no error

        } catch (e: Exception) {
            callback(e)
        }
    }


    suspend fun fetchPhotos(userId: String): List<Photo> {
        return firestore.collection("artists").document(userId).collection("photos").get()
            .await().documents.mapNotNull { it.toObject(Photo::class.java)?.copy(id = it.id) }
    }

    suspend fun fetchVideos(userId: String): List<Video> {
        return firestore.collection("artists").document(userId).collection("videos").get()
            .await().documents.mapNotNull { it.toObject(Video::class.java)?.copy(id = it.id) }
    }


    suspend fun deleteMedia(mediaId: String, isVideo: Boolean) {
        try {
            val userId = utilities.getCurrentUserId()
            val mediaType = if (isVideo) "videos" else "photos"

            // Fetch media document to get file URL
            val mediaDoc = firestore.collection("artists").document(userId).collection(mediaType)
                .document(mediaId).get().await()

            if (mediaDoc.exists()) {
                val fileUrl = if (isVideo) {
                    mediaDoc.getString("videoUrl")
                } else {
                    mediaDoc.getString("imageUrl")
                }

                // Delete from Firestore
                firestore.collection("artists").document(userId).collection(mediaType)
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
