package com.example.talenta.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ExpertAvailability(
    val timezone: String, // e.g., "Asia/Kolkata" For
    val schedule: Map<DaysOfMonth, TimeSlot>
){}

@Serializable
data class DaysOfMonth(
    val month: Int, // 1-12
    val year: Int, // e.g., 2023
    val days: List<Int> // e.g., [1, 2, 3, ..., 31]
){}

@Serializable
data class TimeSlot(
    val start: String, // e.g., "15:00" in 24hr format (HH:mm)
    val end: String    // e.g., "17:00"
){}
