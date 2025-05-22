package com.example.talenta// Test class for extension functions in BookingViewModel.kt

import com.example.talenta.data.model.DateSlot
import com.example.talenta.data.model.DaysOfMonth
import com.example.talenta.data.model.ExpertAvailability
import com.example.talenta.data.model.TimeSlot
import com.example.talenta.presentation.expertBooking.getDateTimeSlotsMap
import com.example.talenta.presentation.expertBooking.getTimeSlots
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class BookingViewModelExtensionsTest {

    @Test
    fun `getDateTimeSlotsMap returns correct TimeSlot for matching date`() {
        val timeSlot = TimeSlot(start = "09:00", end = "12:00")
        val dateSlot = DateSlot(
            startDateTime = "2024-06-15T09:00:00Z",
            endDateTime = "2024-06-15T12:00:00Z"
        )

        val expertAvailability = ExpertAvailability(
            timezone = "Asia/Kolkata",
            schedule = listOf(dateSlot)
        )

        val result = expertAvailability.getDateTimeSlotsMap(15, 6, 2024)
        assertNotEquals(timeSlot, result)
    }

    @Test
    fun `getDateTimeSlotsMap returns null for non-matching date`() {
        val timeSlot = TimeSlot(start = "09:00", end = "12:00")
        val daysOfMonth = DaysOfMonth(
            month = 6,
            year = 2024,
            days = listOf(10, 11, 12)
        )
        val startLocalDateTime = LocalDateTime(
            year = 2024,
            monthNumber = 6,
            dayOfMonth = 15,
            hour = 8,
            minute = 0,
            second = 0
        )
        val expertAvailability = ExpertAvailability(
            timezone = "Asia/Kolkata",
            schedule = mapOf(daysOfMonth to timeSlot)
        )

        val result = expertAvailability.getDateTimeSlotsMap(15, 6, 2024)
        assertEquals(null, result)
    }

    @Test
    fun `getTimeSlots returns correct hourly slots`() {
        val timeSlot = TimeSlot(start = "09:00", end = "12:00")
        val expected = listOf("09:00", "10:00", "11:00", "12:00")
        val result = timeSlot.getTimeSlots()
        assertEquals(expected, result)
    }
}