package com.example.talenta.utils

import timber.log.Timber


sealed class FirestoreResult<out T>(
    val data: T? = null, val errorMessage: String? = null
) {
    class Success<T>(data: T) : FirestoreResult<T>(data)
    class Failure<T>(errorMessage: String?, data: T? = null) :
        FirestoreResult<T>(data, errorMessage)
}

suspend fun <T> safeFirebaseCall(
    block: suspend () -> T
): FirestoreResult<T> {
    return try {
        FirestoreResult.Success(block())
    } catch (e: Exception) {
        Timber.tag("Firebase Call Error").e(e)
        FirestoreResult.Failure(e.message ?: "Unknown error occurred")
    }
}

