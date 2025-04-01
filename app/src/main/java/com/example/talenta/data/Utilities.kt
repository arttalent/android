package com.example.talenta.data

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Utilities @Inject constructor(
    @ApplicationContext private val context: Context,
    private val auth: FirebaseAuth
) {
    fun isValidSize(fileUri: Uri, maxSizeMB: Double): Boolean {
        val cursor =
            context.contentResolver.query(fileUri, arrayOf(OpenableColumns.SIZE), null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val fileSizeInBytes = it.getLong(it.getColumnIndexOrThrow(OpenableColumns.SIZE))
                return fileSizeInBytes <= maxSizeMB * 1024 * 1024
            }
        }
        return false // Returning false if unable to determine size
    }

    suspend fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw Exception("User not authenticated")
    }
}
