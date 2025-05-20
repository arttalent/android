package com.example.talenta.data.repository

import com.example.talenta.data.model.DayOfWeek
import com.example.talenta.data.model.ExpertAvailability
import com.example.talenta.data.model.TimeSlot
import com.example.talenta.data.model.User
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
    @Named("users") private val userCollection: CollectionReference,
    @Named("expertAvailability") private val expertAvailabilityCollection: CollectionReference,
) {

    suspend fun fetchExperts(): FirestoreResult<List<User>> = withContext(Dispatchers.IO) {
        safeFirebaseCall {
            val snapshot = userCollection.whereEqualTo("role", "EXPERT").get().await()
            snapshot.documents.mapNotNull { snapshot ->
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

    suspend fun setExpertAvailability(
        expertId: String,
        weeklySchedule: Map<DayOfWeek, List<TimeSlot>>,
        timezone: String,
    ): FirestoreResult<Void> = withContext(Dispatchers.IO) {
        safeFirebaseCall {
            expertAvailabilityCollection.document(expertId).set(
                ExpertAvailability(
                    timezone = timezone,
                    weeklySchedule = weeklySchedule,
                ),
            ).await()
        }
    }

    suspend fun getExpertAvailability(expertId: String): FirestoreResult<ExpertAvailability?> =
        withContext(Dispatchers.IO) {
            safeFirebaseCall {
                val snapshot = expertAvailabilityCollection.document(expertId).get().await()
                snapshot.toObject(ExpertAvailability::class.java)
            }
        }
}
