package com.example.talenta.data.model

import androidx.annotation.Keep
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.time.format.DateTimeFormatter

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
data class DaysOfMonth(
    val month: Int, // 1-12
    val year: Int, // e.g., 2023
    val days: List<Int> // e.g., [1, 2, 3, ..., 31]
) {}

@Serializable
data class DateSlot(
    val startDateTime: String = "", // e.g., "2023-10-01T15:00:00Z" in UTC ISO 8601 format
    val endDateTime: String=""    // e.g., "2023-10-01T17:00:00Z"
) {

    fun localStartDateTime(): LocalDateTime {
        val timeZone = TimeZone.currentSystemDefault()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy") // match your input format
        val localDate = java.time.LocalDate.parse(startDateTime, formatter)
        val javaLocalDateTime = localDate.atStartOfDay()
        val instant = javaLocalDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant()
        return instant.toKotlinInstant().toLocalDateTime(timeZone)
    }


    fun localEndDateTime(): LocalDateTime {
        val timeZone = TimeZone.currentSystemDefault()
        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val localDate = java.time.LocalDate.parse(endDateTime, formatter)
        val javaLocalDateTime = localDate.atStartOfDay()
        val instant = javaLocalDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant()
        return instant.toKotlinInstant().toLocalDateTime(timeZone)
    }

}


@Serializable
data class TimeSlot(
    val start: String = "", // e.g., "15:00" in 24hr format (HH:mm)
    val end: String  =""  // e.g., "17:00"
) {}


