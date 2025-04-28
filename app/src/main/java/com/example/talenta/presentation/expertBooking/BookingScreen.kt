package com.example.talenta.presentation.expertBooking

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talenta.ui.theme.TalentATheme
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ExpertBookingScreen(modifier: Modifier = Modifier) {
    val selectedDate = rememberSaveable(saver = LocalDateSaver) {
        mutableStateOf(LocalDate.now())
    }
    Column(Modifier.padding(top = 40.dp)) {
        CustomCalender() {
            selectedDate.value = it
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = Color.LightGray.copy(alpha = 0.5f),
            thickness = 2.dp
        )
        Text(
            text = selectedDate.value.toPrettyString(),
            modifier = Modifier.padding(16.dp),
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        FlowRow(
            modifier = Modifier
                .scrollable(
                    orientation = Orientation.Vertical,
                    state = rememberScrollState(),
                )
                .fillMaxWidth()
                .fillMaxHeight(
                    fraction = 0.3f
                )
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            maxItemsInEachRow = 3,
        ) {
            listOf(
                "12:00 PM",
                "12:30 PM",
                "01:00 PM",
                "01:30 PM",
                "02:00 PM",
                "02:30 PM",
                "03:00 PM",
                "03:30 PM",
                "04:00 PM",
                "04:30 PM",
            ).forEach { time ->
                DateSlot(
                    text = time,
                    selected = false,
                    enabled = true,
                    onClick = {}
                )
            }
        }
    }

}

fun LocalDate.toPrettyString(): String {
    val dayOfWeek = this.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val dayOfMonth = this.dayOfMonth
    val suffix = when {
        dayOfMonth in 11..13 -> "th"
        dayOfMonth % 10 == 1 -> "st"
        dayOfMonth % 10 == 2 -> "nd"
        dayOfMonth % 10 == 3 -> "rd"
        else -> "th"
    }
    val month = this.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    return "$dayOfWeek, ${dayOfMonth}$suffix $month"
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ExpertBookingScreenPRev() {
    TalentATheme {
        ExpertBookingScreen()
    }
}