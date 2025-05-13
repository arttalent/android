package com.example.talenta.data.model

data class Booking(
    val bookingId: String,
    val expertId: String, // uid of the current user
    val artistId: String, // uid of the current user
    val serviceId: String = "", //should get the service id from the service collection
    val scheduledStartTime: String = "", // UTC ISO 8601 format
    val timeInHrs: Int,   // UTC ISO 8601 format
    val status: BookingStatus = BookingStatus.PENDING,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val report: Report? = null, // Expert assessment (structure to be defined)
)

enum class BookingStatus {
    PENDING, ACCEPTED, CONFIRMED, COMPLETED, CANCELLED, UNATTENDED
}

enum class PaymentStatus {
    PENDING, PAID, NOT_PAID
}

data class Feedback(
    val rating: Float = 0f, val review: String = "", val timestamp: String = "" // UTC ISO 8601
)

class Report // To be defined later
