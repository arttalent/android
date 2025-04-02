package com.example.talenta.presentation.state

sealed class AuthUiStatee {
    object Idle : AuthUiStatee()
    object Loading : AuthUiStatee()
    object Success : AuthUiStatee()
    object OtpSent : AuthUiStatee()
    data class Error(val message: String) : AuthUiStatee()
}