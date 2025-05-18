package com.example.talenta.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val serviceId: String,
    val serviceType: ServiceType,
    val serviceTitle: String = serviceType.getTitle(),
    val perHourCharge: Float = 0.0f,
    val isActive: Boolean = true,
    val expertAvailability: ExpertAvailability
){}

enum class ServiceType {
    LIVE_ASSESSMENT,
    TRAINING,
    ADVISE,
    VIDEO_ASSESSMENT
}

fun ServiceType.getTitle(): String {
    return when (this) {
        ServiceType.LIVE_ASSESSMENT -> "Online Live Assessment"
        ServiceType.VIDEO_ASSESSMENT -> "Online Video Assessment"
        ServiceType.TRAINING -> "Online Training"
        ServiceType.ADVISE -> "Online Advise"
    }
}



