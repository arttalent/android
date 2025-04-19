package com.example.talenta.data.repository

import com.example.talenta.data.model.User
import com.example.talenta.utils.FirestoreResult
import com.example.talenta.utils.safeFirebaseCall
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class ExpertScreenRepository @Inject constructor(
    @Named("users")
    private val userCollection: CollectionReference
) {

    suspend fun fetchExperts(): FirestoreResult<List<User>> = withContext(Dispatchers.IO) {
        safeFirebaseCall {
            val query = userCollection.whereEqualTo("role", "EXPERT")
            val snapshot = query.get().await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(User::class.java)
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
}
