package com.example.talenta.presentation.myBookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.UserPreferences
import com.example.talenta.data.model.Booking
import com.example.talenta.data.model.Role
import com.example.talenta.data.model.User
import com.example.talenta.data.repository.AuthRepository
import com.example.talenta.data.repository.BookingRepository
import com.example.talenta.utils.FirestoreResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyBookingStates(
    val isLoading: Boolean = false,
    val bookings: List<Booking> = emptyList(),
    val users: List<User> = emptyList(),
    val currentUser : User? = null,
    val error: String? = null
)

@HiltViewModel
class MyBookingsViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val _states = MutableStateFlow(MyBookingStates())
    val states = _states.asStateFlow()

    fun fetchBookings() {
        viewModelScope.launch {
            _states.update {
                it.copy(isLoading = true)
            }
            val userId = userPreferences.getUserData()?.id ?: return@launch
            val role = userPreferences.getUserData()?.role ?: return@launch

            val response = bookingRepository.getBookingsForUser(userId, role.toString())
            when (response) {
                is FirestoreResult.Success -> {
                    val role = userPreferences.getUserData()?.role ?: return@launch

                    val userIdList = response.data?.map {
                        if (role == Role.ARTIST) {
                            it.expertId
                        } else {
                            it.artistId
                        }
                    } ?: emptyList()

                    val userResponse = authRepository.getUsers(userIdList)
                    when (userResponse) {
                        is FirestoreResult.Success -> {
                            _states.update {
                                it.copy(
                                    isLoading = false,
                                    bookings = response.data ?: emptyList(),
                                    users = userResponse.data ?: emptyList()
                                )
                            }
                        }

                        is FirestoreResult.Failure -> {
                            _states.update {
                                it.copy(
                                    isLoading = false,
                                    error = userResponse.errorMessage
                                )
                            }
                        }
                    }
                }

                is FirestoreResult.Failure -> {
                    _states.update {
                        it.copy(
                            isLoading = false,
                            error = response.errorMessage
                        )
                    }
                }
            }
        }
    }

    fun updateBookingStatus(bookingId: String, newStatus: String) {

    }
}