package com.example.talenta.presentation.bookingDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.UserPreferences
import com.example.talenta.data.model.Booking
import com.example.talenta.data.model.BookingStatus
import com.example.talenta.data.model.PaymentStatus
import com.example.talenta.data.model.Role
import com.example.talenta.data.model.User
import com.example.talenta.data.repository.BookingRepository
import com.example.talenta.utils.FirestoreResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

data class BookingDetailsState(
    val isLoading: Boolean = false,
    val booking: Booking? = null,
    val user: User? = null,
    val currentUser: User? = null,
    val error: String? = null
)

sealed class BookingDetailsActions {
    data class InitData(val booking: Booking, val user: User, val currentUser: User) :
        BookingDetailsActions()

    object ResetError : BookingDetailsActions()

    data class ExpertAcceptRejectBooking(val accept: Boolean) : BookingDetailsActions()
    data object ArtistPaymentDoneForBooking : BookingDetailsActions()
    data class ExpertRescheduleRequestForBooking(val newBookingStartDateTime: LocalDateTime) :
        BookingDetailsActions()


}

@HiltViewModel
class BookingDetailsViewmodel @Inject constructor(
    private val bookingDetailsRepository: BookingRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiStates = MutableStateFlow(BookingDetailsState())
    val uiStates = _uiStates.asStateFlow()


    fun onAction(action: BookingDetailsActions) {
        when (action) {
            is BookingDetailsActions.InitData -> {
                _uiStates.value = _uiStates.value.copy(
                    booking = action.booking,
                    user = action.user,
                    currentUser = action.currentUser
                )
            }

            BookingDetailsActions.ResetError -> {
                _uiStates.value = _uiStates.value.copy(error = null)
            }

            BookingDetailsActions.ArtistPaymentDoneForBooking -> {
                changePaymentStatus(
                    isPaid = true
                )
            }

            is BookingDetailsActions.ExpertAcceptRejectBooking -> {
                acceptOrRejectBooking(action.accept)
            }

            is BookingDetailsActions.ExpertRescheduleRequestForBooking -> {
                rescheduleBooking(action.newBookingStartDateTime)
            }
        }
    }

    fun refreshBookingDetails() {
        viewModelScope.launch {
            val booking = _uiStates.value.booking ?: return@launch
            fetchBookingById(booking.bookingId)
        }
    }

    suspend fun fetchBookingById(bookingId: String) {
        _uiStates.update {
            it.copy(
                isLoading = true,
            )
        }
        bookingDetailsRepository.getBookingsById(bookingId).let { result ->
            when (result) {
                is FirestoreResult.Success -> {
                    _uiStates.update {
                        it.copy(
                            booking = result.data,
                            isLoading = false
                        )
                    }
                }

                is FirestoreResult.Failure -> {
                    _uiStates.update {
                        it.copy(
                            error = result.errorMessage,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun rescheduleBooking(newStartDateTime: LocalDateTime) {
        viewModelScope.launch {
            val booking = _uiStates.value.booking ?: return@launch
            bookingDetailsRepository.changeStartDateTime(
                bookingId = booking.bookingId,
                newStartDateTime = newStartDateTime.toString()
            ).let { result ->
                when (result) {
                    is FirestoreResult.Success -> {
                        _uiStates.update {
                            it.copy(
                                booking = _uiStates.value.booking?.copy(
                                    scheduledStartTime = newStartDateTime.toString(),
                                    status = BookingStatus.RESCHEDULED
                                )
                            )
                        }
                    }

                    is FirestoreResult.Failure -> {
                        _uiStates.update {
                            it.copy(
                                isLoading = false,
                                error = result.errorMessage
                            )
                        }
                    }
                }
            }
        }
    }

    fun acceptOrRejectBooking(accept: Boolean) {
        viewModelScope.launch {
            val booking = _uiStates.value.booking ?: return@launch
            val currentUser = _uiStates.value.currentUser ?: return@launch

            if (currentUser.role != Role.EXPERT) {
                _uiStates.value =
                    _uiStates.value.copy(error = "Only artists can accept or reject bookings")
                return@launch
            }

            val status = if (accept) {
                BookingStatus.ACCEPTED
            } else {
                BookingStatus.REJECTED
            }
            bookingDetailsRepository.updateBookingStatus(booking.bookingId, status).let { result ->
                when (result) {
                    is FirestoreResult.Success -> {
                        _uiStates.value = _uiStates.value.copy(
                            booking = _uiStates.value.booking?.copy(status = status),
                            error = null
                        )
                    }

                    is FirestoreResult.Failure -> {
                        _uiStates.value = _uiStates.value.copy(error = result.errorMessage)
                    }
                }
            }
        }

    }

    fun changePaymentStatus(isPaid: Boolean) {
        val paymentStatus = if (isPaid) {
            PaymentStatus.PAID
        } else {
            PaymentStatus.PENDING
        }
        val bookingId = _uiStates.value.booking?.bookingId ?: return
        viewModelScope.launch {
            bookingDetailsRepository.updatePaymentStatus(bookingId, paymentStatus).let { result ->
                when (result) {
                    is FirestoreResult.Success -> {
                        _uiStates.value = _uiStates.value.copy(
                            booking = _uiStates.value.booking?.copy(paymentStatus = paymentStatus),
                            error = null
                        )
                    }

                    is FirestoreResult.Failure -> {
                        _uiStates.value = _uiStates.value.copy(error = result.errorMessage)
                    }
                }
            }
        }
    }


}