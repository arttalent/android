package com.example.talenta.data.model

// Data classes for media items
data class Photo(
    val id: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class Artist(
    val id: String = "", val person: Person = Person()
)


data class Video(
    val id: String = "",
    val videoUrl: String = "",
    val thumbnailUrl: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)


data class SocialMediaLinks(
    val facebook: String = "",
    val instagram: String = "",
    val linkedin: String = "",
    val twitter: String = "",
)

data class Certificate(
    val id: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)


