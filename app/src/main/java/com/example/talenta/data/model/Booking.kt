package com.example.talenta.data.model

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

    val createdAt: String = "", // UTC ISO 8601 format,
    val updatedAt: String = "", // UTC ISO 8601 format
) {}

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
