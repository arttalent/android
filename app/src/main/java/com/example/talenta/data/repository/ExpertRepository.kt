package com.example.talenta.data.repository

import com.example.talenta.data.UserPreferences
import com.example.talenta.data.model.ExpertAvailability
import com.example.talenta.data.model.Schedule
import com.example.talenta.data.model.Service
import com.example.talenta.data.model.ServiceType
import com.example.talenta.data.model.User
import com.example.talenta.data.model.getTitle
import com.example.talenta.utils.FirestoreResult
import com.example.talenta.utils.safeFirebaseCall
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
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
}
