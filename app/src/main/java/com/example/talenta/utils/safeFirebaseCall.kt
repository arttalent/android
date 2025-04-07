package com.example.talenta.utils


import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

sealed class FirestoreResult<out T> {
    data class Success<T>(val data: T) : FirestoreResult<T>()
    data class Failure(val errorMessage: String) : FirestoreResult<Nothing>()
}

suspend fun <T> safeFirebaseCall(
    block: suspend () -> T
): FirestoreResult<T> {
    return try {
        FirestoreResult.Success(block())
    } catch (e: Exception) {
        FirestoreResult.Failure(e.message ?: "Unknown error occurred")
    }
}

