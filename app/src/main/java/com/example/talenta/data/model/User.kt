package com.example.talenta.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.Serializable

@Keep
@IgnoreExtraProperties
data class User(
    val id: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val physicalAttributes: PhysicalAttributes = PhysicalAttributes(),
    val profilePicture: String = "",
    val bio: Bio = Bio(),
    val role: Role? = null,
    val isVerified: Boolean? = null,
    val isBlocked: Boolean? = null,
    val professionalData: ProfessionalData = ProfessionalData(),
) {
}

@Serializable
enum class Ethnicity {
    WHITE,
    BLACK,
    HISPANIC,
    ASIAN,
    SOUTH_ASIAN,
    MIDDLE_EASTERN,
    NATIVE_AMERICAN,
    PACIFIC_ISLANDER,
    MIXED,
    OTHER
}

@Serializable
data class ProfessionalData(
    val profession: String = "",
    val subProfession: String = "",
    val media: List<Media> = emptyList(),
    val skills: List<String> = emptyList(),
    val certifications: List<String> = emptyList(),
    val certificatesList: List<Certificate> = emptyList(),
){}

@Serializable
data class Bio(
    val city: String = "",
    val country: String = "",
    val bioData: String = "",
    val language: String = "",
    val socialMediaLinks: SocialMediaLinks = SocialMediaLinks(),
){}

@Serializable
data class PhysicalAttributes(
    val height: String = "",
    val weight: String = "",
    val gender: String = "",
    val age: Int = 0,
    val ethnicity: Ethnicity? = null,
    val color: String = "",
){}

@Serializable
data class Media(
    val url: String = "",
    val type: MediaType? = null,
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
){}

@Serializable
enum class MediaType {
    IMAGE,
    VIDEO
}


@Serializable
data class SocialMediaLinks(
    val facebook: String = "",
    val instagram: String = "",
    val linkedin: String = "",
    val twitter: String = "",
){}

@Serializable
data class Certificate(
    val id: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
){}



