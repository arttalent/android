package com.example.talenta.presentation.ui.screens

import com.example.talenta.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HostRepository @Inject constructor(
    val firestore: FirebaseFirestore
) {


    suspend fun getRole(userId: String): String? = withContext(Dispatchers.IO) {
        try {
            val artistDoc = firestore.collection("users").document(userId).get().await()

            if (artistDoc.exists()) {
                try {
                    val user = artistDoc.toObject(User::class.java)
                    if (user != null) {
                        return@withContext user.role.toString()
                    }
                } catch (_: Exception) {
                }
            }
            null
        } catch (e: Exception) {
            null
        }
    }
}