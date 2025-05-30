package com.example.talenta.data.repository

import android.net.Uri
import com.example.talenta.data.UserPreferences
import com.example.talenta.data.model.ExpertAvailability
import com.example.talenta.data.model.Media
import com.example.talenta.data.model.MediaType
import com.example.talenta.data.model.Schedule
import com.example.talenta.data.model.Service
import com.example.talenta.data.model.ServiceType
import com.example.talenta.data.model.User
import com.example.talenta.data.model.getTitle
import com.example.talenta.utils.FirestoreResult
import com.example.talenta.utils.safeFirebaseCall
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ExpertRepository @Inject constructor(
    @Named("users")
    private val userCollection: CollectionReference,
    @Named("expertAvailability")
    private val expertAvailabilityCollection: CollectionReference,
    private val storage: FirebaseStorage,
    private val userPreferences: UserPreferences,
) {

    suspend fun fetchExperts(): FirestoreResult<List<User>> = withContext(Dispatchers.IO) {
        safeFirebaseCall {
            val snapshot = userCollection.whereEqualTo("role", "EXPERT").get().await()
            snapshot.documents.mapNotNull { snapshot ->
                Timber.tag("ExpertRepository").d("fetchExperts: %s", snapshot.data)
                try {
                    snapshot.toObject(User::class.java)
                } catch (e: Exception) {
                    Timber.tag("ExpertRepository").e(e, "Error parsing expert data")
                    null
                }
            }
        }
    }

    suspend fun getExpertById(expertId: String): FirestoreResult<User?> =
        withContext(Dispatchers.IO) {
            safeFirebaseCall {
                val snapshot = userCollection.document(expertId).get().await()
                snapshot.toObject(User::class.java)
            }
        }

    fun getCurrentUserProfile(refresh: Boolean): Flow<User?> = flow {
        // If refresh is requested, fetch from remote first

        // Then emit the flow from local storage
        userPreferences.getUserDataFlow().collect { user ->
            if (user == null && !refresh) {
                // Only fetch from remote if we haven't already done so due to refresh
                Timber.e("User data is null, fetching from remote")
                getCurrentUserProfileFromRemote()
            } else {
                Timber.d("Current user profile fetched: %s", user)
            }
            emit(user)
            if (refresh) {
                getCurrentUserProfileFromRemote()
            }

        }
    }

    suspend fun getCurrentUserProfileFromRemote() {
        val userId = userPreferences.getUserData()?.id ?: return
        val userRes = safeFirebaseCall {
            val snapshot = userCollection.document(userId).get().await()
            snapshot.toObject(User::class.java)
        }
        when (userRes) {
            is FirestoreResult.Success -> {
                userRes.data?.let { user ->
                    userPreferences.saveUserData(user)
                }
            }

            is FirestoreResult.Failure -> {
                Timber.e("Failed to fetch user profile: ${userRes.errorMessage}")
            }
        }
    }


    suspend fun getExpertAvailability(expertId: String): FirestoreResult<ExpertAvailability?> =
        withContext(Dispatchers.IO) {
            safeFirebaseCall {
                val snapshot = expertAvailabilityCollection.document(expertId).get().await()
                snapshot.toObject(ExpertAvailability::class.java)
            }
        }

    suspend fun createExpertService(
        perHrPrice: Float,
        serviceType: ServiceType,
        schedule: Schedule,
    ): FirestoreResult<Unit> = withContext(Dispatchers.IO) {
        val expertId = userPreferences.getUserData()?.id ?: ""
        val serviceId = expertId + (userPreferences.getUserData()?.expertService?.size ?: 0)
        val oldServices = userPreferences.getUserData()?.expertService ?: emptyList()
        val serviceList = oldServices.toMutableList()
        val service = Service(
            serviceId = serviceId,
            serviceTitle = serviceType.getTitle(),
            perHourCharge = perHrPrice,
            serviceType = serviceType,
            expertAvailability = ExpertAvailability(
                timezone = TimeZone.currentSystemDefault().toString(),
                schedule = listOf(schedule)
            ),
            isActive = true
        )
        serviceList.add(service)
        safeFirebaseCall {
            userCollection.document(expertId)
                .update(mapOf(User::expertService.name to serviceList))
                .await()
            Unit
        }
    }

    suspend fun uploadMedia(
        imageUri: Uri,
        description: String,
        mediaType: MediaType
    ): FirestoreResult<Unit> {
        val userId = userPreferences.getUserData()?.id ?: ""
        val fileRef = storage.reference.child("$mediaType/$userId/${description+System.currentTimeMillis()}")

        fileRef.putFile(imageUri).await()
        val downloadUrl = fileRef.downloadUrl.await().toString()

        val oldProfessionalData = userPreferences.getUserData()?.professionalData

        val mediaData = Media(
            type = mediaType,
            url = downloadUrl,
            description = description,
            timestamp = System.currentTimeMillis()
        )

        val newMediaList = oldProfessionalData?.media?.toMutableList() ?: mutableListOf()
        newMediaList.add(mediaData)

       val newData =  oldProfessionalData?.copy(
            media = newMediaList.toList()
        )

        val result = safeFirebaseCall {
            userCollection.document(userId)
                .update(mapOf(User::professionalData.name to newData))
                .await()
            Unit
        }

        return result


    }

    suspend fun deleteMedia(media: Media): FirestoreResult<Unit> {
        val userId = userPreferences.getUserData()?.id ?: ""
        val fileRef = storage.getReferenceFromUrl(media.url)


        val fileDeletionResult = safeFirebaseCall {
            fileRef.delete().await()
            Unit
        }
        when (fileDeletionResult) {
            is FirestoreResult.Failure -> {
                Timber.e("Failed to delete media file: ${fileDeletionResult.errorMessage}")
                return fileDeletionResult
            }

            is FirestoreResult.Success -> {
                return safeFirebaseCall {
                    userCollection.document(userId)
                        .update(
                            mapOf(
                                User::professionalData.name to
                                        userPreferences.getUserData()?.professionalData?.copy(
                                            media = userPreferences.getUserData()?.professionalData?.media?.filterNot { it.url == media.url }
                                                ?: emptyList()
                                        )
                            )).await()
                    Unit
                }

            }
        }


    }


    suspend fun uploadProfilePicture(
        imageUri: Uri
    ): FirestoreResult<Unit> {
        val userId = userPreferences.getUserData()?.id ?: ""
        val fileRef = storage.reference.child("profileImages/$userId")

        fileRef.putFile(imageUri).await()
        val downloadUrl = fileRef.downloadUrl.await().toString()

        val result = safeFirebaseCall {
            userCollection.document(userId)
                .update(mapOf(User::profilePicture.name to downloadUrl))
                .await()
            Unit
        }

        return result


    }
}
