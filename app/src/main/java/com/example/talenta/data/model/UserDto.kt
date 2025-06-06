package com.example.talenta.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val physicalAttributes: PhysicalAttributesDto = PhysicalAttributesDto(),
    val profilePicture: String = "",
    val bio: BioDto = BioDto(),
    val role: RoleDto? = null,
    val isVerified: Boolean? = null,
    val isBlocked: Boolean? = null,
    val professionalData: ProfessionalDataDto = ProfessionalDataDto(),
    val sponsorDetails: SponsorDetailsDto? = null,
    val expertService: List<ServiceDto>? = null
)

@Serializable
data class SponsorDetailsDto(
    val sponsorType: SponsorTypeDto = SponsorTypeDto.INDIVIDUAL,
    val profileInterests: List<String> = emptyList(),
    val companyName: String = "",
    val address: String = ""
)

@Serializable
data class PhysicalAttributesDto(
    val height: String = "",
    val weight: String = "",
    val gender: String = "",
    val age: Int = 0,
    val ethnicity: EthnicityDto? = null,
    val color: String = ""
)

@Serializable
data class BioDto(
    val city: String = "",
    val country: String = "",
    val bioData: String = "",
    val language: String = "",
    val socialMediaLinks: SocialMediaLinksDto = SocialMediaLinksDto()
)

@Serializable
data class SocialMediaLinksDto(
    val facebook: String = "",
    val instagram: String = "",
    val linkedin: String = "",
    val twitter: String = ""
)

@Serializable
data class ProfessionalDataDto(
    val profession: String = "",
    val subProfession: String = "",
    val skills: List<String> = emptyList(),
    val media: List<MediaDto> = emptyList(),
    val certifications: List<String> = emptyList(),
    val certificatesList: List<CertificateDto> = emptyList()
)

@Serializable
data class MediaDto(
    val url: String = "",
    val type: MediaTypeDto? = null,
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class CertificateDto(
    val id: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)


@Serializable
data class ServiceDto(
    val id: String = "", val name: String = "", val description: String = ""
)

@Serializable
enum class RoleDto {
    ARTIST, EXPERT, SPONSOR, ADMIN
}

@Serializable
enum class MediaTypeDto {
    IMAGE, VIDEO
}

@Serializable
enum class EthnicityDto {
    WHITE, BLACK, HISPANIC, ASIAN, SOUTH_ASIAN, MIDDLE_EASTERN, NATIVE_AMERICAN, PACIFIC_ISLANDER, MIXED, OTHER
}

@Serializable
enum class SponsorTypeDto {
    INDIVIDUAL, COMPANY
}

data class Users(
    val id: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val bio: BioDto = BioDto(),
    val phoneNumber: String = "",
    val profilePicture: String = "",
    val role: RoleDto? = null,
    val profession: String = "",
    val subProfession: String = "",
    val skills: List<String> = emptyList(),
    val physicalAttributes: PhysicalAttributesDto = PhysicalAttributesDto(),
)


fun UserDto.toUsers(): Users {
    return Users(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        bio = this.bio,
        profilePicture = this.profilePicture,
        phoneNumber = this.phoneNumber,
        role = this.role,
        profession = this.professionalData.profession,
        subProfession = this.professionalData.subProfession,
        skills = this.professionalData.skills,
        physicalAttributes = this.physicalAttributes
    )
}

