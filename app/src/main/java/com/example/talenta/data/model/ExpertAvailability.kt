package com.example.talenta.data.model

import androidx.annotation.Keep
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.serialization.Serializable

@Serializable
data class ExpertAvailability(
    val timezone: String = "", // e.g., "Asia/Kolkata" For
    val schedule: List<Schedule> = emptyList<Schedule>()
) {}

@Serializable
@Keep
data class Schedule(
    val dateSlot: DateSlot = DateSlot(), // e.g., "2023-10-01T15:00:00Z" in UTC ISO 8601 format
    val timeSlot: TimeSlot = TimeSlot() // e.g., "15:00" in 24hr format (HH:mm)
) {}

@Serializable
data class DateSlot(
    val startDateTime: String = "", // e.g., "2023-10-01T15:00:00Z" in UTC ISO 8601 format
    val endDateTime: String=""    // e.g., "2023-10-01T17:00:00Z"
) {

    fun localStartDateTime(): LocalDateTime {
        val formatter = LocalDate.Format {
            dayOfMonth(Padding.ZERO)
            char('/')
            monthNumber(Padding.ZERO)
            char('/')
            year(Padding.ZERO)
        }
        return LocalDate.parse(startDateTime, formatter).atTime(0, 0)
    }

    fun localEndDateTime(): LocalDateTime {
        val formatter = LocalDate.Format {
            dayOfMonth(Padding.ZERO)
            char('/')
            monthNumber(Padding.ZERO)
            char('/')
            year(Padding.ZERO)
        }
        return LocalDate.parse(endDateTime, formatter).atTime(0, 0)
    }
}


@Serializable
data class TimeSlot(
    val start: String = "", // e.g., "15:00" in 24hr format (HH:mm)
    val end: String  =""  // e.g., "17:00"
) {}


