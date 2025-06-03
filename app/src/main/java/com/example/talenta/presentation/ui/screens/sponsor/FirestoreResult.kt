package com.example.talenta.presentation.ui.screens.sponsor

sealed class FirestoreResult<out T> {
    data class Success<out T>(val data: T) : FirestoreResult<T>()
    data class Error(val exception: Exception) : FirestoreResult<Nothing>()
    data class Failure(val message: String) : FirestoreResult<Nothing>()
    object Loading : FirestoreResult<Nothing>()
}
