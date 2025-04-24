package com.example.talenta.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.Serializable

// Data classes for media items
data class Photo(
    val id: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)


data class Video(
    val id: String = "",
    val videoUrl: String = "",
    val thumbnailUrl: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)



