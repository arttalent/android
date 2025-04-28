package com.example.talenta.presentation.expertBooking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.DayOfWeek
import com.example.talenta.data.model.ExpertAvailability
import com.example.talenta.data.repository.ExpertRepository
import com.example.talenta.utils.FirestoreResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalTime
import kotlinx.datetime.toLocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import javax.inject.Inject


@HiltViewModel
class BookingViewModel @Inject constructor(
    private val expertRepository: ExpertRepository,
    private val expertId: String,
) : ViewModel() {

    fun fetchExpertAvailability() {
        viewModelScope.launch {
            val result = expertRepository.getExpertAvailability(expertId)
            when (result) {
                is FirestoreResult.Success -> {
                    // Handle success
                    val availability = result.data

                    // Do something with the availability data
                }

                is FirestoreResult.Failure -> {
                    // Handle error
                    val errorMessage = result.errorMessage
                    // Show error message to the user
                }
            }
        }

    }

    fun getConvertedHourlySlots(
        expertAvailability: ExpertAvailability,
        userTimeZone: TimeZone,
        day: DayOfWeek
    ): List<String> {
        val slots = expertAvailability.weeklySchedule[day] ?: return emptyList()

        val expertTimeZone = TimeZone.of(expertAvailability.timezone)
        val today = Clock.System.now().toLocalDateTime(expertTimeZone).date

        val result = mutableListOf<String>()

        for (slot in slots) {
            var current = LocalTime.parse(slot.start)
            val end = LocalTime.parse(slot.end)

            while (current < end) {
                val expertDateTime = LocalDateTime(
                    year = today.year,
                    monthNumber = today.monthNumber,
                    dayOfMonth = today.dayOfMonth,
                    hour = current.hour,
                    minute = current.minute
                )
                val instant = expertDateTime.toInstant(expertTimeZone)
                val userDateTime = instant.toLocalDateTime(userTimeZone)

                val formatted = "%02d:%02d".format(userDateTime.hour, userDateTime.minute)
                result.add(formatted)

                current = current.addHours(1)
            }
        }

        return result
    }

    fun LocalTime.addHours(hoursToAdd: Int): LocalTime {
        val dummyDate = LocalDate(2000, 1, 1) // any safe dummy date
        val dummyDateTime = LocalDateTime(dummyDate, this)
        val updated =
            dummyDateTime.toJavaLocalDateTime().plus(hoursToAdd.toLong(), ChronoUnit.HOURS)
        return updated.toLocalTime().toKotlinLocalTime()
    }


}