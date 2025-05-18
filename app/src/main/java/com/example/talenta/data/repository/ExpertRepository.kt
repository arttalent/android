package com.example.talenta.data.repository

import android.util.Log
import com.example.talenta.data.UserPreferences
import com.example.talenta.data.model.ExpertAvailability
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
                Log.d("TAG", "fetchExperts: " + snapshot.data)
                snapshot.toObject(User::class.java)
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
        serviceID: String,
        perHrPrice: Float,
        serviceType: ServiceType,
        expertAvailability: ExpertAvailability
    ): FirestoreResult<Unit> = withContext(Dispatchers.IO) {
        val service = Service(
            serviceId = serviceID,
            serviceTitle = serviceType.getTitle(),
            perHourCharge = perHrPrice,
            serviceType = serviceType,
            expertAvailability = expertAvailability,
            isActive = true
        )
        safeFirebaseCall {
            val expertId = userPreferences.getUserData()?.id ?: ""
            expertAvailabilityCollection.document(expertId).set(service).await()
            Unit
        }
    }
}
