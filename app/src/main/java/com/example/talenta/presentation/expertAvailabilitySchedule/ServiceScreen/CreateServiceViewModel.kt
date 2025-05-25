package com.example.talenta.presentation.expertAvailabilitySchedule.ServiceScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.ExpertAvailability
import com.example.talenta.data.model.Service
import com.example.talenta.data.model.ServiceType
import com.example.talenta.data.model.DateSlot
import com.example.talenta.data.model.TimeSlot
import com.example.talenta.data.model.getTitle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*

data class CreateServiceUiState(
    val selectedServiceType: ServiceType? = null,
    val hourlyPay: String = "",
    val selectedStartDate: Long? = null,
    val selectedEndDate: Long? = null,
    val selectedStartTime: String = "00:00",
    val selectedEndTime: String = "24:00",
    val selectedTimezone: String = TimeZone.currentSystemDefault().id,
    val additionalNotes: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isServiceCreated: Boolean = false
)

class CreateServiceViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

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

    fun updateSelectedDates(startDate: Long?, endDate: Long?) {
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
            currentState.selectedStartDate!! > currentState.selectedEndDate!! -> "End date must be after start date"
            !isValidTimeRange(currentState.selectedStartTime, currentState.selectedEndTime) -> "End time must be after start time"
            else -> null
        }

        if (errorMessage != null) {
            _uiState.value = currentState.copy(errorMessage = errorMessage)
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

    private fun createDateTimeString(dateMillis: Long, time: String, timezone: String): String {
        // Convert milliseconds to LocalDate
        val instant = Instant.fromEpochMilliseconds(dateMillis)
        val localDate = instant.toLocalDateTime(TimeZone.of(timezone)).date

        // Parse time
        val timeParts = time.split(":")
        val hour = if (time == "24:00") 0 else timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        // Create LocalDateTime
        val localDateTime = LocalDateTime(
            year = localDate.year,
            month = localDate.month,
            dayOfMonth = localDate.dayOfMonth,
            hour = hour,
            minute = minute
        )

        // Convert to UTC Instant and return ISO string
        val utcInstant = localDateTime.toInstant(TimeZone.of(timezone))
        return utcInstant.toString()
    }

    private fun createService() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                val currentState = _uiState.value
                val serviceId = UUID.randomUUID().toString()

                // Create DateSlot with UTC ISO 8601 format
                val startDateTime = createDateTimeString(
                    currentState.selectedStartDate!!,
                    currentState.selectedStartTime,
                    currentState.selectedTimezone
                )

                val endDateTime = createDateTimeString(
                    currentState.selectedEndDate!!,
                    if (currentState.selectedEndTime == "24:00") "23:59" else currentState.selectedEndTime,
                    currentState.selectedTimezone
                )

                val dateSlot = DateSlot(
                    startDateTime = startDateTime,
                    endDateTime = endDateTime
                )

                val timeSlot = TimeSlot(
                    start = currentState.selectedStartTime,
                    end = currentState.selectedEndTime
                )

                val expertAvailability = ExpertAvailability(
                    timezone = currentState.selectedTimezone,
                    schedule = mapOf(dateSlot to timeSlot)
                )

                val service = Service(
                    serviceId = serviceId,
                    serviceType = currentState.selectedServiceType!!,
                    serviceTitle = currentState.selectedServiceType.getTitle(),
                    perHourCharge = currentState.hourlyPay.toFloat(),
                    isActive = true,
                    expertAvailability = expertAvailability
                )

                // Save to Firebase
                firestore.collection("services")
                    .document(serviceId)
                    .set(service)
                    .await()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isServiceCreated = true
                )

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