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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.talenta.data.model.User
import com.example.talenta.ui.theme.TalentATheme
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ExpertBooking(
    expertDetails: User,
) {
    val viewModel = hiltViewModel<BookingViewModel>()
    val uiState = viewModel.uiStates.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.onAction(
            BookingActions.InitData(
                expertDetails.id ?: "",
                expertDetails.expertService?.serviceId ?: ""
            )
        )
    }
    ExpertBookingScreen(
        uiState = uiState,
        action = viewModel::onAction
    )
}


@Composable
fun ExpertBookingScreen(
    modifier: Modifier = Modifier,
    uiState: BookingStates,
    action: (BookingActions) -> Unit
) {
    val selectedDate = rememberSaveable(saver = LocalDateSaver) {
        mutableStateOf(LocalDate.now())
    }
    Column(Modifier.padding(top = 40.dp)) {
        CustomCalender() {
            selectedDate.value = it
            action(BookingActions.OnDateSelected(it.toKotlinLocalDate()))
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
                )
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            maxItemsInEachRow = 3,
        ) {
            get24HourList().forEach { time ->
                DateSlot(
                    modifier = Modifier.fillMaxWidth(0.26f),
                    text = time,
                    selected = false,
                    enabled = true,
                    onClick = {
                        action(BookingActions.OnTimeSelected(stringToLocalTime(time)))
                    }
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

fun get24HourList(): List<String> {
    val formatter = DateTimeFormatter.ofPattern("HH:00")
    return (8..20).map { hour ->
        LocalTime(hour, 0).toJavaLocalTime().format(formatter)
    }
}

fun stringToLocalTime(timeString: String): LocalTime {
    val parts = timeString.split(":")
    val hour = parts[0].toInt()
    val minute = parts[1].toInt()
    return LocalTime(hour, minute)
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ExpertBookingScreenPRev() {
    TalentATheme {
        ExpertBookingScreen(
            uiState = BookingStates(),
            action = { }
        )
    }
}