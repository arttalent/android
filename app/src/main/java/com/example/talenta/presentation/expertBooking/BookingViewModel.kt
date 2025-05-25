package com.example.talenta.presentation.expertBooking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.ExpertAvailability
import com.example.talenta.data.model.Service
import com.example.talenta.data.model.TimeSlot
import com.example.talenta.data.model.User
import com.example.talenta.data.repository.BookingRepository
import com.example.talenta.utils.FirestoreResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalTime
import javax.inject.Inject


sealed class BookingActions {
    data class OnDateSelected(val date: LocalDate) : BookingActions()
    data object ResetError : BookingActions()
    data class OnTimeSelected(val time: LocalTime?) : BookingActions()
    data class InitData(val expertDetails: User, val selectedServiceId:String) : BookingActions()
    data object CreateBooking : BookingActions()
}

data class BookingStates(
    val expertDetails: User? = null,
    val selectedDate: LocalDate? = null,
    val selectedTime: LocalTime? = null,
    val timeSlotBySelectedDate: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val selectedService :Service ? = null,
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
                val timeSlot =
                    uiStates.value.selectedService?.expertAvailability?.getDateTimeSlotsMap(
                        day = action.date.dayOfMonth,
                        month = action.date.monthNumber,
                        year = action.date.year
                    )

                _uiStates.update {
                    it.copy(
                        selectedDate = action.date,
                        timeSlotBySelectedDate = timeSlot?.getTimeSlots() ?: emptyList(),
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
                        expertDetails = action.expertDetails,
                        selectedService = action.expertDetails.expertService?.find { service ->
                            service.serviceId == action.selectedServiceId
                        },
                    )
                }
            }

            BookingActions.CreateBooking -> {
                createInitialBookingFromArtist()
            }
        }
    }

    private fun createInitialBookingFromArtist() {
        val time = uiStates.value.selectedTime
        val date = uiStates.value.selectedDate
        val localInstant =
            time?.let { date?.atTime(it)?.toInstant(TimeZone.currentSystemDefault()) }
        val startTime = localInstant?.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET)

        viewModelScope.launch {
            _uiStates.update {
                it.copy(
                    isLoading = true
                )
            }
            _uiStates.value.expertDetails?.let { expert ->
                val result = bookingRepository.createBooking(
                    expertId = expert.id ?: "",
                    serviceId = _uiStates.value.selectedService?.serviceId
                        ?: "", // Replace with actual service ID
                    scheduleStartTime = startTime ?: "N/A",
                    hours = "1" // Replace with actual hours
                )
                when (result) {
                    is FirestoreResult.Success -> {
                        // Handle success
                        _uiStates.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }

                    is FirestoreResult.Failure -> {
                        // Handle error
                        _uiStates.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                }

            }
        }
    }




}

fun ExpertAvailability.getDateTimeSlotsMap(day: Int, month: Int, year: Int): TimeSlot? {
    val dateTime = LocalDateTime(year, month, day, 0, 0, 0)
    var dateTimeSlot:TimeSlot ? = null
    schedule.forEach {
        val dateSlot = it.dateSlot
        val timeSlot = it.timeSlot
        val startDateTime = dateSlot.localStartDateTime().toJavaLocalDateTime()
        val endDateTime = dateSlot.localEndDateTime().toJavaLocalDateTime()
        if (dateTime.toJavaLocalDateTime().isAfter(startDateTime) && dateTime.toJavaLocalDateTime().isBefore(endDateTime)) {
            dateTimeSlot = timeSlot
        }
    }
    return dateTimeSlot
}

fun TimeSlot.getTimeSlots(): List<String> {
    val startTime = LocalTime.parse(start)
    val endTime = LocalTime.parse(end)
    val timeSlots = mutableListOf<String>()
    var currentTime = startTime

    while (currentTime <= endTime) {
        timeSlots.add(currentTime.toString())
        currentTime = currentTime.toJavaLocalTime().plusHours(1).toKotlinLocalTime()
    }
    return timeSlots
}