package com.example.talenta.data.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

data class Booking(
    val bookingId: String = "",
    val expertId: String = "",
    val artistId: String = "",
    val serviceId: String = "",

    val scheduledStartTime: String = "", // UTC ISO 8601 format
    val timeInHrs: Int = 0,   // UTC ISO 8601 format

    val status: BookingStatus = BookingStatus.PENDING,
    val paymentStatus: PaymentStatus = PaymentStatus.NOT_PAID,
    val report: Report? = null, // Expert assessment (structure to be defined)

    val createdAt: Long = System.currentTimeMillis(), // UTC ISO 8601 format,
    val updatedAt:Long = System.currentTimeMillis(), // UTC ISO 8601 format
) {
    fun prettyStartDateTime(): String {
        // Parse ISO8601 string to Instant
        val instant = Instant.parse(scheduledStartTime)

        // Convert to LocalDateTime in current system timezone
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        // Create formatter using DSL for "12th May 2025 12:30 PM"
        val formatter = LocalDateTime.Format {
            // Day with ordinal suffix (manually added after formatting)
            dayOfMonth()
            char(' ')
            monthName(MonthNames.ENGLISH_FULL)
            char(' ')
            year()
            char(' ')
            amPmHour()
            char(':')
            minute()
            char(' ')
            amPmMarker("AM", "PM")
        }

        // Format with DSL
        val baseFormatted = localDateTime.format(formatter)

        // Add ordinal suffix to day
        val day = localDateTime.dayOfMonth
        val dayWithSuffix = when {
            day in 11..13 -> "${day}th"
            day % 10 == 1 -> "${day}st"
            day % 10 == 2 -> "${day}nd"
            day % 10 == 3 -> "${day}rd"
            else -> "${day}th"
        }

        // Replace the day number with ordinal version
        return baseFormatted.replaceFirst(day.toString(), dayWithSuffix)
    }
}

enum class BookingStatus {
    PENDING,
    ACCEPTED,
    CONFIRMED,
    COMPLETED,
    CANCELLED,
    UNATTENDED,
    REJECTED,
    RESCHEDULED,
}

enum class PaymentStatus {
    PENDING,
    PAID,
    NOT_PAID
}

data class Feedback(
    val rating: Float = 0f,
    val review: String = "",
    val timestamp: String = "" // UTC ISO 8601
) {}

class Report // To be defined later
