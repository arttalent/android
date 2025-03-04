package com.example.talenta.data.model

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

// Adding these properties to the Artist class to manage media
data class Artist(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val profession: String = "",
    val subProfession: String = "",
    val countryCode: String = "",
    val mobileNumber: String = "",
    val photoUrl: String = "",
    val gender: String = "",
    val age: Int = 0,
    val birthYear: Int = 0,
    val language: String = "",
    val height: String = "",
    val weight: String = "",
    val ethnicity: String = "",
    val color: String = "",
    val city: String = "",
    val country: String = "",
    val bioData: String = "",
    val socialMediaLinks: SocialMediaLinks = SocialMediaLinks(),
    val certificatesList: List<Certificate> = emptyList(),
    val photos: List<Photo> = emptyList(),
    val videos: List<Video> = emptyList(),
    val skills: List<String> = emptyList()
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


