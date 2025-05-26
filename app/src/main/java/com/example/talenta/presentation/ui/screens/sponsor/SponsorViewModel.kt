package com.example.talenta.presentation.ui.screens.sponsor

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.User
import com.example.talenta.presentation.ui.screens.sponsor.use_cases.GetArtistUseCase
import com.example.talenta.presentation.ui.screens.sponsor.use_cases.GetExpertUseCase
import com.example.talenta.presentation.ui.screens.sponsor.use_cases.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class UserState(
    val isLoading: Boolean = false, val user: List<User> = listOf(User()), val error: String = ""
)

@HiltViewModel
class SponsorViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getArtistUseCase: GetArtistUseCase,
    private val getExpertUseCase: GetExpertUseCase,
) :
    ViewModel() {

    private val _user = mutableStateOf(UserState())
    val user: State<UserState> = _user

    private val _artist = MutableStateFlow(UserState())
    val artist: StateFlow<UserState> = _artist

    private val _expert = MutableStateFlow(UserState())
    val expert: StateFlow<UserState> = _expert

    init {
        getUser()
        getExpert()
        getArtist()
    }

    private fun getExpert() {
        getExpertUseCase().onEach { result ->
            when (result) {
                is FirestoreResult.Loading -> {
                    _expert.value = UserState(isLoading = true)
                }

                is FirestoreResult.Success -> {
                    _expert.value = UserState(user = result.data)

                }

                is FirestoreResult.Error -> {
                    _expert.value =
                        UserState(error = result.exception.localizedMessage ?: "Unknown error")
                }

                is FirestoreResult.Failure -> ""
            }
        }.launchIn(viewModelScope)
    }

    private fun getArtist() {
        getArtistUseCase().onEach { result ->
            when (result) {
                is FirestoreResult.Loading -> {
                    _artist.value = UserState(isLoading = true)
                }

                is FirestoreResult.Success -> {
                    _artist.value = UserState(user = result.data)
                }

                is FirestoreResult.Error -> {
                    _artist.value =
                        UserState(error = result.exception.localizedMessage ?: "Unknown error")
                }

                is FirestoreResult.Failure -> ""
            }
        }.launchIn(viewModelScope)
    }

    private fun getUser() {
        getUserUseCase().onEach { result ->
            when (result) {
                is FirestoreResult.Loading -> {
                    _user.value = UserState(isLoading = true)
                }

                is FirestoreResult.Success -> {
                    _user.value = UserState(user = result.data)
                }

                is FirestoreResult.Error -> {
                    _user.value =
                        UserState(error = result.exception.localizedMessage ?: "Unknown error")
                }

                is FirestoreResult.Failure -> ""
            }
        }.launchIn(viewModelScope)
    }
}