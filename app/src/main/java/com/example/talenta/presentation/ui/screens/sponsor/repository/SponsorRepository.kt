package com.example.talenta.presentation.ui.screens.sponsor.repository

import com.example.talenta.data.model.User
import com.example.talenta.utils.FirestoreResult

interface SponsorRepository {
    suspend fun fetchUser(): FirestoreResult<List<User>>
    suspend fun fetchExpert(): FirestoreResult<List<User>>
    suspend fun fetchArtist(): FirestoreResult<List<User>>
}