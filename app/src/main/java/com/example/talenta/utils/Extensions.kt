package com.example.talenta.utils

import android.content.Context
import android.widget.Toast
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.format.optional
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun LocalDateTime.toIsoString(): String {
    val timeZone = TimeZone.currentSystemDefault()
    val instant = this.toInstant(timeZone)
    return instant.toString()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun formatIsoToFormatterDateTime(isoString: String): String {
    val instant = Instant.parse(isoString)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val formatter = LocalDateTime.Format{
        date(LocalDate.Formats.ISO)
        char(' ')
        hour(); char(':'); minute()
        optional {
            char(':'); second()
            optional {
                char('.'); secondFraction(minLength = 3)
            }
        }
    }
    return localDateTime.format(format = formatter)
}