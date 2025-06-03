package com.example.talenta.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
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

fun Float.toDp(density: Density): Dp = with(density) { this@toDp.toDp() }
