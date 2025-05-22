package com.example.talenta

import com.example.talenta.data.model.DateSlot
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class DateSlotTest {

    @Test
    fun testLocalStartDateTimeConversion() {
        val dateSlot = DateSlot(
            startDateTime = "2023-10-01T15:00:00Z",
            endDateTime = "2023-10-01T17:00:00Z"
        )
        val expectedLocalDateTime = Instant.parse("2023-10-01T15:00:00Z")
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val actualLocalDateTime = dateSlot.localStartDateTime()

        assertEquals(expectedLocalDateTime, actualLocalDateTime)
    }

    @Test
    fun testLocalEndDateTimeConversion() {
        val dateSlot = DateSlot(
            startDateTime = "2023-10-01T15:00:00Z",
            endDateTime = "2023-10-01T17:00:00Z"
        )
        val expectedLocalDateTime = Instant.parse("2023-10-01T17:00:00Z")
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val actualLocalDateTime = dateSlot.localEndDateTime()

        assertEquals(expectedLocalDateTime, actualLocalDateTime)
    }
}