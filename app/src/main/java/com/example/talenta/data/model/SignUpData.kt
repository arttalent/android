package com.example.talenta.data.model

data class SignUpData(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val countryCode: String = "",
    val phoneNumber: String = "",
)