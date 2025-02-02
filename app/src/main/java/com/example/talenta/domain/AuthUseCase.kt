package com.example.talenta.domain

import android.net.Uri
import com.example.talenta.data.model.Artist
import com.example.talenta.data.repository.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun sendOtp(phoneNumber: String) = repository.sendOtp(phoneNumber)
    suspend fun verifyOtp(verificationId: String, code: String) =
        repository.verifyOtp(verificationId, code)
    suspend fun createArtistProfile(artist: Artist, imageUri: Uri?) =
        repository.createArtistProfile(artist, imageUri)
    suspend fun getArtistProfile() = repository.getArtistProfile()
}