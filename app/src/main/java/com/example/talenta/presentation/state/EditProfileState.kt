package com.example.talenta.presentation.state

import com.example.talenta.data.model.Certificate
import com.example.talenta.data.model.Ethnicity
import com.example.talenta.data.model.Media
import com.example.talenta.data.model.Role

sealed class EditProfileState {
    object Loading : EditProfileState()
    data class Success(
        val firstName: String = "",
        val lastName: String = "",
        val phoneNumber: String = "",
        val email: String = "",
        val city: String = "",
        val country: String = "",
        val gender: String = "",
        val certifications: List<String> = emptyList(),
        val certificatesList: List<Certificate> = emptyList(),
        val age: Int = 0,
        val ethnicity: Ethnicity? = null,
        val color: String = "",
        val profession: String = "",
        val subProfession: String = "",
        val media: List<Media> = emptyList(),
        val skills: List<String> = emptyList(),
        var profilePicture: String = "",
        var isVerified: Boolean? = null,
        var blocked: Boolean? = null,
        var role: Role? = null,
        val birthYear: String = "",
        val language: String = "",
        val height: String = "",
        val weight: String = "",
        val bioData: String = "",
        val facebook: String = "",
        val instagram: String = "",
        val linkedin: String = "",
        val twitter: String = "",
        val certificates: List<Certificate> = emptyList()
    ) : EditProfileState()

    data class Error(val message: String) : EditProfileState()
}