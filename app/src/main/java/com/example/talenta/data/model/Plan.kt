package com.example.talenta.data.model

// This file defines the Plan data class and ServiceType enum class.
data class Plan(
    val planId: String = "",
    val expertId: String = "", // UID of the expert
    val planTitle: String = "",
    val description: String = "",
    val serviceType: ServiceType = ServiceType.ADVISE,
    val perHourCharge: Float = 0.0f,
    val currency: String = "USD", // Currency code
    val isActive: Boolean = true
){}

enum class ServiceType {
    VIDEO_ASSESSMENT,
    LIVE_ASSESSMENT,
    ADVISE
}

