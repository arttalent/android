package com.example.talenta.data.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ExpertAvailability(
    val timezone: String, // e.g., "Asia/Kolkata" For
    val schedule: Map<DateSlot,TimeSlot>
) {}

@Serializable
data class DaysOfMonth(
    val month: Int, // 1-12
    val year: Int, // e.g., 2023
    val days: List<Int> // e.g., [1, 2, 3, ..., 31]
) {}

@Serializable
data class DateSlot(
    val startDateTime: String, // e.g., "2023-10-01T15:00:00Z" in UTC ISO 8601 format
    val endDateTime: String    // e.g., "2023-10-01T17:00:00Z"
) {

    fun localStartDateTime(): LocalDateTime
    {
        val timeZone = TimeZone.currentSystemDefault() // Replace with the actual timezone
        val instant = Instant.parse(startDateTime) // parse ISO8601 string to Instant
        return instant.toLocalDateTime(timeZone)
    }
    fun localEndDateTime(): LocalDateTime
    {
        val timeZone = TimeZone.currentSystemDefault() // Replace with the actual timezone
        val instant = Instant.parse(endDateTime) // parse ISO8601 string to Instant
        return instant.toLocalDateTime(timeZone)
    }
}



@Serializable
data class TimeSlot(
    val start: String, // e.g., "15:00" in 24hr format (HH:mm)
    val end: String    // e.g., "17:00"
) {}


