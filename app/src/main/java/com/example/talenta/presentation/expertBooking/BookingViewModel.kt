package com.example.talenta.presentation.expertBooking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.repository.BookingRepository
import com.example.talenta.utils.FirestoreResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.toInstant
import javax.inject.Inject


sealed class BookingActions {
    data class OnDateSelected(val date: LocalDate) : BookingActions()
    data object ResetError : BookingActions()
    data class OnTimeSelected(val time: LocalTime) : BookingActions()
    data class InitData(val expertId: String, val serviceId: String) : BookingActions()
    data object CreateBooking : BookingActions()
}

data class BookingStates(
    val expertId: String = "",
    val serviceId: String = "",
    val selectedDate: LocalDate? = null,
    val selectedTime: LocalTime? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
) : ViewModel() {

    private val _uiStates = MutableStateFlow(BookingStates())
    val uiStates = _uiStates.asStateFlow()

    fun onAction(action: BookingActions) {
        when (action) {
            BookingActions.ResetError -> {
                _uiStates.update {
                    it.copy(
                        errorMessage = null
                    )
                }
            }

            is BookingActions.OnDateSelected -> {
                _uiStates.update {
                    it.copy(
                        selectedDate = action.date
                    )
                }
            }

            is BookingActions.OnTimeSelected -> {
                _uiStates.update {
                    it.copy(
                        selectedTime = action.time
                    )
                }
            }

            is BookingActions.InitData -> {
                _uiStates.update {
                    it.copy(
                        expertId = action.expertId,
                        serviceId = action.serviceId
                    )
                }
            }

            BookingActions.CreateBooking -> {
                createInitialBookingFromArtist()
            }
        }
    }

    fun createInitialBookingFromArtist() {
        val time = uiStates.value.selectedTime
        val date = uiStates.value.selectedDate
        val localInstant =
            time?.let { date?.atTime(it)?.toInstant(TimeZone.currentSystemDefault()) }
        val startTime = localInstant?.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET)

        viewModelScope.launch {
            val result = bookingRepository.createBooking(
                expertId = _uiStates.value.expertId,
                serviceId = _uiStates.value.serviceId, // Replace with actual service ID
                scheduleStartTime = startTime ?: "N/A",
                hours = "1" // Replace with actual hours
            )
            when (result) {
                is FirestoreResult.Success -> {
                    // Handle success
                }

                is FirestoreResult.Failure -> {
                    // Handle error
                }
            }
        }
    }


}