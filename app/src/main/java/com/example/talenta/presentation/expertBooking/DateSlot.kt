package com.example.talenta.presentation.expertBooking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.talenta.ui.theme.TalentATheme


@Composable
fun DateSlot(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit = {}
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) MaterialTheme.colorScheme.primary else Gray
        ),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
    ) {
        Text(
            text = text,
            color = if (enabled) Black else Gray
        )
    }
}

@Preview
@Composable
private fun DateSlotPrev() {
    TalentATheme {
        DateSlot(
            text = "12:30pm",
            selected = true,
            enabled = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun DisabledDateSlotPrev() {
    TalentATheme {
        DateSlot(
            text = "12:30pm",
            selected = false,
            enabled = false,
            onClick = {}
        )
    }
}