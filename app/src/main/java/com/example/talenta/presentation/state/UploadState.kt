package com.example.talenta.presentation.state

sealed class UploadState {
    data object Idle : UploadState()
    data object Loading : UploadState()
    data class Success(val downloadUrl: String) : UploadState()
    data class Error(val message: String) : UploadState()
}
