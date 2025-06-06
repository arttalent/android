package com.example.talenta.data.model

import kotlinx.serialization.Serializable

// we have to add the genre field in the user class, as genre is completely different from the skills, as well as instruments
@Serializable
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
    val sponsorDetails: SponsorDetails? = null,
    val expertService: List<Service>? = null,
) {
    val isArtist: Boolean
        get() = role == Role.ARTIST

    val isExpert: Boolean
        get() = role == Role.EXPERT

    val fullName: String
        get() = "$firstName $lastName"
}

enum class Profession {
    MOVIE_DIRECTOR, SINGER, MOVIE_ACTOR, DANCER
}

fun Profession.getTitle(): String {
    return when (this) {
        Profession.MOVIE_DIRECTOR -> "Movie Director"
        Profession.SINGER -> "Singer"
        Profession.MOVIE_ACTOR -> "Movie Actor"
        Profession.DANCER -> "Dancer"
    }
}

@Serializable
data class SponsorDetails(
    val sponsorType: SponsorType = SponsorType.INDIVIDUAL,
    val profileInterests: List<String> = emptyList(),
    val companyName: String = "",
    val address: String = ""
)


@Serializable
enum class SponsorType {
    INDIVIDUAL, COMPANY
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
)

// for the skill we can use the enum class
enum class Skill {
    GUITAR, SINGER, PIANIST, THEORY_TEACHING, INSTRUMENTAL_TEACHING, MUSIC_PSYCHOLOGY, PERFECT_PITCH
}

@Serializable
data class Bio(
    val city: String = "",
    val country: String = "",
    val bioData: String = "",
    val language: String = "",
    val socialMediaLinks: SocialMediaLinks = SocialMediaLinks(),
)

@Serializable
data class PhysicalAttributes(
    val height: String = "",
    val weight: String = "",
    val gender: String = "",
    val age: Int = 0,
    val ethnicity: Ethnicity? = null,
    val color: String = "",
)

@Serializable
data class Media(
    val url: String = "",
    val type: MediaType? = null,
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

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
)


// we should add a title as well for the certificate i think
@Serializable
data class Certificate(
    val id: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)


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