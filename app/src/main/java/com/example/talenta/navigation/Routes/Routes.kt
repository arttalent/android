package com.example.talenta.navigation.Routes

sealed class Route(val path: String) {
    object Onboarding : Route("onboarding")
    object Auth : Route("auth")
    object SignUpAs : Route("signup_as")
    object SignUp : Route("signup")
    object OTPVerification : Route("verify_otp")
    object Success : Route("success")
    object Login : Route("login")
    object ForgotPassword : Route("forgot_password")
    object PasswordResetSuccess : Route("password_reset_success")
    object EditProfile : Route("edit_profile")
}