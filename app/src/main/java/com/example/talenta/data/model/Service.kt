package com.example.talenta.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val serviceId: String = "",
    val serviceTitle: String = "",
    val serviceType: ServiceType = ServiceType.ADVISE,
    val perHourCharge: Float = 0.0f,
    val isActive: Boolean = true,
    val expertAvailability: ExpertAvailability
){}

enum class ServiceType {
    ASSESSMENT,
    TRAINING,
    ADVISE
}



