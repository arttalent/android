package com.example.talenta.presentation.ui.screens.sponsor

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.User
import com.example.talenta.presentation.ui.screens.sponsor.use_cases.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class UserState(
    val isLoading: Boolean = false, val user: List<User> = listOf(User()), val error: String = ""
)

@HiltViewModel
class SponsorViewModel @Inject constructor(private val getUserUseCase: GetUserUseCase) :
    ViewModel() {

    private val _user = mutableStateOf(UserState())
    val user: State<UserState> = _user

    init {
        getUser()
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