package com.example.talenta.presentation.ui.screens.sponsor.use_cases

import com.example.talenta.data.model.User
import com.example.talenta.presentation.ui.screens.sponsor.FirestoreResult
import com.example.talenta.presentation.ui.screens.sponsor.repository.SponsorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetArtistUseCase @Inject constructor(private val sponsorRepository: SponsorRepository) {

    operator fun invoke(): Flow<FirestoreResult<List<User>>> = flow {
        emit(FirestoreResult.Loading)

        when (val result = sponsorRepository.fetchArtist()) {
            else -> {
                val artist = result.data
                if (artist != null) {
                    emit(FirestoreResult.Success<List<User>>(artist))
                } else {
                    emit(FirestoreResult.Error(Exception("Data type mismatch")))
                }
            }
        }
    }
}