package com.example.talenta.presentation.expertBooking

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.talenta.data.model.DateSlot
import com.example.talenta.data.model.ExpertAvailability
import com.example.talenta.data.model.Schedule
import com.example.talenta.data.model.Service
import com.example.talenta.data.model.ServiceType
import com.example.talenta.data.model.TimeSlot
import com.example.talenta.data.model.User
import com.example.talenta.ui.theme.TalentATheme
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ExpertBooking(
    expertDetails: User, selectedServiceId: String, onBookingDone:()-> Unit
) {
    val viewModel = hiltViewModel<BookingViewModel>()
    val uiState = viewModel.uiStates.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.onAction(
            BookingActions.InitData(
                expertDetails = expertDetails, selectedServiceId = selectedServiceId
            )
        )
    }
    if (uiState.onBookingComplete) {
        LaunchedEffect(Unit) {
            viewModel.onAction(BookingActions.ResetError)
            onBookingDone()
        }

    }
    ExpertBookingScreen(
        uiState = uiState, action = viewModel::onAction
    )
}


@Composable
fun ExpertBookingScreen(
    modifier: Modifier = Modifier, uiState: BookingStates, action: (BookingActions) -> Unit
) {
    val selectedDate = rememberSaveable(saver = LocalDateSaver) {
        mutableStateOf(LocalDate.now())
    }
    Box {
        Column(Modifier.padding(top = 10.dp)) {
            CustomCalender(
                expertAvailability = uiState.selectedService?.expertAvailability
            ) {
                selectedDate.value = it
                action(BookingActions.OnDateSelected(it.toKotlinLocalDate()))
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = Color.LightGray.copy(alpha = 0.5f),
                thickness = 2.dp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = selectedDate.value.toPrettyString(),
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = uiState.selectedService?.expertAvailability?.timezone ?: "UTC",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            val timeSlotsByDate = uiState.timeSlotBySelectedDate
            if (timeSlotsByDate.isEmpty()) {
                LaunchedEffect(Unit) {
                    action(BookingActions.OnTimeSelected(null))
                }
                Text(
                    text = "No time slots available",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    color = Color.Black,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                TimeSlotsRow(
                    modifier = Modifier.padding(bottom = 16.dp),
                    timeSlots = timeSlotsByDate,
                    selectedTime = uiState.selectedTime,
                    action = action
                )
            }
        }

        if (uiState.selectedTime != null) {
            BookingBottomSheet(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                expertName = uiState.expertDetails?.firstName + " " + uiState.expertDetails?.lastName,
                formattedDateTime = convertIntoLocalDateTime(
                    date = selectedDate.value.toKotlinLocalDate(),
                    hr = uiState.selectedTime.hour,
                    min = uiState.selectedTime.minute,
                    expertTimeZone = uiState.selectedService?.expertAvailability?.timezone ?: "UTC"
                ),
                fees = "$${uiState.selectedService?.perHourCharge}",
                loading = uiState.isLoading,
                onConfirmClick = {
                    action(BookingActions.CreateBooking)
                }
            )
        }

    }

}

@Composable
fun TimeSlotsRow(
    modifier: Modifier = Modifier,
    selectedTime: LocalTime?,
    timeSlots: List<String>,
    action: (BookingActions) -> Unit
) {
    FlowRow(
        modifier = modifier
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
        timeSlots.forEach { time ->
            val selected = selectedTime?.let {
                it.hour == stringToLocalTime(time).hour && it.minute == stringToLocalTime(time).minute
            } == true
            DateSlot(
                modifier = Modifier.fillMaxWidth(0.26f),
                text = time,
                selected = selected,
                enabled = true,
                onClick = {
                    action(BookingActions.OnTimeSelected(stringToLocalTime(time)))
                })
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

fun stringToLocalTime(timeString: String): LocalTime {
    val parts = timeString.split(":")
    val hour = parts[0].toInt()
    val minute = parts[1].toInt()
    return LocalTime(hour, minute)
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ExpertBookingScreenPRev() {
    val service = Service(
        serviceId = "1",
        serviceType = ServiceType.VIDEO_ASSESSMENT,
        perHourCharge = 50.04f,
        expertAvailability = ExpertAvailability(
            timezone = "Asia/Kolkata", schedule = listOf(Schedule(
                DateSlot(
                    startDateTime = "2023-10-01T00:00:00Z", endDateTime = "2023-11-10T00:00:00Z"
                ), TimeSlot(
                    start = "15:00", end = "17:00"
                )
            ))
        )
    )
    TalentATheme {
        ExpertBookingScreen(
            uiState = BookingStates(
                expertDetails = User(
                    firstName = "John", lastName = "Doe", expertService = listOf(
                        service
                    )

                ),
                selectedDate = LocalDate.now().toKotlinLocalDate(),
                selectedTime = LocalTime(10, 0),
                timeSlotBySelectedDate = listOf("10:00", "11:00", "12:00")
            ), action = { })
    }
}