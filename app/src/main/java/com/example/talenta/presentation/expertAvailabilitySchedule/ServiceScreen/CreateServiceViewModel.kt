package com.example.talenta.presentation.expertAvailabilitySchedule.ServiceScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.DateSlot
import com.example.talenta.data.model.Schedule
import com.example.talenta.data.model.ServiceType
import com.example.talenta.data.model.TimeSlot
import com.example.talenta.data.repository.ExpertRepository
import com.example.talenta.utils.FirestoreResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import javax.inject.Inject

data class CreateServiceUiState(
    val selectedServiceType: ServiceType? = null,
    val hourlyPay: String = "",
    val selectedStartDate: LocalDateTime? = null,
    val selectedEndDate: LocalDateTime? = null,
    val selectedStartTime: String = "00:00",
    val selectedEndTime: String = "24:00",
    val selectedTimezone: String = TimeZone.currentSystemDefault().id,
    val additionalNotes: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isServiceCreated: Boolean = false
)

@HiltViewModel
class CreateServiceViewModel @Inject constructor(
    private val expertRepo: ExpertRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateServiceUiState())
    val uiState: StateFlow<CreateServiceUiState> = _uiState.asStateFlow()

    fun updateServiceType(serviceType: ServiceType) {
        _uiState.value = _uiState.value.copy(selectedServiceType = serviceType)
    }

    fun updateHourlyPay(pay: String) {
        // Only allow numeric input with decimal
        val filteredPay = pay.filter { it.isDigit() || it == '.' }
        _uiState.value = _uiState.value.copy(hourlyPay = filteredPay)
    }

    fun updateSelectedDates(startDate: LocalDateTime?, endDate: LocalDateTime?) {
        _uiState.value = _uiState.value.copy(
            selectedStartDate = startDate,
            selectedEndDate = endDate
        )
    }

    fun updateStartTime(time: String) {
        _uiState.value = _uiState.value.copy(selectedStartTime = time)
    }

    fun updateEndTime(time: String) {
        _uiState.value = _uiState.value.copy(selectedEndTime = time)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun validateAndCreateService() {
        val currentState = _uiState.value

        // Validation
        val errorMessage = when {
            currentState.selectedServiceType == null -> "Please select a service type"
            currentState.hourlyPay.isEmpty() -> "Please enter hourly pay"
            currentState.hourlyPay.toFloatOrNull() == null -> "Please enter a valid hourly pay"
            currentState.hourlyPay.toFloat() <= 0 -> "Hourly pay must be greater than 0"
            currentState.selectedStartDate == null -> "Please select start date"
            currentState.selectedEndDate == null -> "Please select end date"
            currentState.selectedStartDate > currentState.selectedEndDate -> "End date must be after start date"
            !isValidTimeRange(
                currentState.selectedStartTime,
                currentState.selectedEndTime
            ) -> "End time must be after start time"

            else -> null
        }

        if (errorMessage != null) {
            _uiState.value = currentState.copy(errorMessage = errorMessage)
            print("Validation Error: $errorMessage")
            return
        }

        createService()
    }

    private fun isValidTimeRange(startTime: String, endTime: String): Boolean {
        if (endTime == "24:00") return true

        val startHour = startTime.split(":")[0].toInt()
        val startMinute = startTime.split(":")[1].toInt()
        val endHour = endTime.split(":")[0].toInt()
        val endMinute = endTime.split(":")[1].toInt()

        val startTotalMinutes = startHour * 60 + startMinute
        val endTotalMinutes = endHour * 60 + endMinute

        return endTotalMinutes > startTotalMinutes
    }

    private fun createDateTimeString(localDateTime:LocalDateTime?): String {
        // Convert milliseconds to LocalDate
        val formatter = LocalDateTime.Format {
            dayOfMonth(Padding.ZERO)
            char('/')
            monthNumber(Padding.ZERO)
            char('/')
            year(Padding.ZERO)
        }
        // Convert to UTC Instant and return ISO string
        val utcInstant = localDateTime?.format(formatter)
        return utcInstant?:"NoDateTime"
    }

    private fun createService() {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null,
                        isServiceCreated = false
                    )
                }

                val currentState = _uiState.value

                // Create DateSlot with UTC ISO 8601 format
                val startDateTime = createDateTimeString(
                    currentState.selectedStartDate!!
                )

                val endDateTime = createDateTimeString(
                    currentState.selectedEndDate!!
                )

                val dateSlot = DateSlot(
                    startDateTime = startDateTime,
                    endDateTime = endDateTime
                )

                val timeSlot = TimeSlot(
                    start = currentState.selectedStartTime,
                    end = currentState.selectedEndTime
                )

                val schedule = Schedule(
                    dateSlot = dateSlot,
                    timeSlot = timeSlot
                )
                // Save to Firebase
                viewModelScope.launch {
                    val res = expertRepo.createExpertService(
                        serviceType = currentState.selectedServiceType!!,
                        perHrPrice = currentState.hourlyPay.toFloat(),
                        schedule = schedule,
                    )
                    when (res) {
                        is FirestoreResult.Failure -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isServiceCreated = false,
                                    errorMessage = "Failed to create service: ${res.errorMessage}"
                                )
                            }
                        }

                        is FirestoreResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isServiceCreated = true,
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to create service: ${e.message}"
                )
            }
        }
    }

    fun resetServiceCreated() {
        _uiState.value = _uiState.value.copy(isServiceCreated = false)
    }

}