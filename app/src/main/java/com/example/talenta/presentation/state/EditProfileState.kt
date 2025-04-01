package com.example.talenta.presentation.state

import com.example.talenta.data.model.Certificate

sealed class EditProfileState {
    object Loading : EditProfileState()
    data class Success(
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val profession: String = "",
        val city: String = "",
        val country: String = "",
        val gender: String = "",
        val age: String = "",
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