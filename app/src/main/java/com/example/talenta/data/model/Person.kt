package com.example.talenta.data.model

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class Person(
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
) {
    constructor() : this(
        firstName = "", lastName = "", email = "", password = "",
        profession = "", subProfession = "", countryCode = "", mobileNumber = "",
        photoUrl = "", gender = "", age = 0, birthYear = 0, language = "",
        height = "", weight = "", ethnicity = "", color = "", city = "", country = "",
        bioData = "", socialMediaLinks = SocialMediaLinks(), certificatesList = emptyList(),
        photos = emptyList(), videos = emptyList(), skills = emptyList()
    )

}
