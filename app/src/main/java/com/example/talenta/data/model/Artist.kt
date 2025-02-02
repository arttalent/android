package com.example.talenta.data.model

data class Artist(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
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
    val color: String = "",
    val city: String = "",
    val country: String = "",
    val bioData: String = "",
    val socialMediaLinks: SocialMediaLinks = SocialMediaLinks()
)
data class SocialMediaLinks(
    val facebook: String = "",
    val instagram: String = "",
    val linkedin: String = ""
)