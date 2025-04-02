package com.example.talenta.presentation.state

sealed class EditProfileEvent {
    object NavigateBack : EditProfileEvent()
    data class ShowError(val message: String) : EditProfileEvent()
    object ShowSuccessMessage : EditProfileEvent()

    // New event specifically for certificate operations that shouldn't navigate
    object CertificateOperationSuccess : EditProfileEvent()
}
