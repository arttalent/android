package com.example.talenta.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ExpertAvailability(
    val timezone: String, // e.g., "Asia/Kolkata" For
    val weeklySchedule: Map<DayOfWeek, List<TimeSlot>>
)

@Serializable
data class TimeSlot(
    val start: String, // e.g., "15:00" in 24hr format (HH:mm)
    val end: String    // e.g., "17:00"
)

enum class DayOfWeek {
    SUN, MON, TUE, WED, THU, FRI, SAT
}
