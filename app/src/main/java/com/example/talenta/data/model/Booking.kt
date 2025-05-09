package com.example.talenta.data.model

data class Booking(
    val bookingId: String,
    val expertId: String,
    val artistId: String,
    val serviceId: String = "",

    val scheduledStartTime: String = "", // UTC ISO 8601 format
    val timeInHrs: Int,   // UTC ISO 8601 format

    val status: BookingStatus = BookingStatus.PENDING,
    val report: Report? = null, // Expert assessment (structure to be defined)

){}

enum class BookingStatus {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED,
    UNATTENDED
}

data class Feedback(
    val rating: Float = 0f,
    val review: String = "",
    val timestamp: String = "" // UTC ISO 8601
){}

class Report // To be defined later
