package com.example.talenta.utils

import android.content.Context
import android.widget.Toast
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

fun LocalDateTime.toIsoString(): String {
    val timeZone = TimeZone.currentSystemDefault()
    val instant = this.toInstant(timeZone)
    return instant.toString()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}