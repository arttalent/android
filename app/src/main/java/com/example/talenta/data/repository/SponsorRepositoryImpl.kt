package com.example.talenta.data.repository

import com.example.talenta.data.model.User
import com.example.talenta.presentation.ui.screens.sponsor.repository.SponsorRepository
import com.example.talenta.utils.FirestoreResult
import com.example.talenta.utils.safeFirebaseCall
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class SponsorRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    @Named("users") private val userCollection: CollectionReference,
) : SponsorRepository {

    override suspend fun fetchExpert(): FirestoreResult<List<User>> = withContext(Dispatchers.IO) {
        safeFirebaseCall {
            val snapshot = userCollection.whereEqualTo("role", "EXPERT").get().await()
            snapshot.documents.mapNotNull { snapshot ->
                snapshot.toObject(User::class.java)
            }
        }
    }

    override suspend fun fetchUser(): FirestoreResult<List<User>> = withContext(Dispatchers.IO) {
        safeFirebaseCall {
            val snapshot = userCollection.get().await()
            snapshot.documents.mapNotNull { snapshot ->
                snapshot.toObject(User::class.java)
            }
        }
    }


    override suspend fun fetchArtist(): FirestoreResult<List<User>> = withContext(Dispatchers.IO) {
        safeFirebaseCall {
            val snapshot = userCollection.whereEqualTo("role", "ARTIST").get().await()
            snapshot.documents.mapNotNull { snapshot ->
                snapshot.toObject(User::class.java)
            }
        }
    }

}