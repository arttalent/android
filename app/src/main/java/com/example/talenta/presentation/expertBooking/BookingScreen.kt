package com.example.talenta.presentation.expertBooking

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.talenta.R
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(modifier = Modifier.padding(top = 10.dp)) {
            // Calendar Section
            CustomCalender(
                expertAvailability = uiState.selectedService?.expertAvailability
            ) {
                selectedDate.value = it
                action(BookingActions.OnDateSelected(it.toKotlinLocalDate()))
            }

            // Elegant Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    thickness = 1.dp
                )
            }

            // Date and Timezone Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.calendar),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = selectedDate.value?.toPrettyString().toString(),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.time),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = uiState.selectedService?.expertAvailability?.timezone ?: "UTC",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val timeSlotsByDate = uiState.timeSlotBySelectedDate
            if (timeSlotsByDate.isEmpty()) {
                LaunchedEffect(Unit) {
                    action(BookingActions.OnTimeSelected(null))
                }

                // Empty State Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.calendar),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No time slots available",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Please select another date",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            } else {
                TimeSlotsRow(
                    modifier = Modifier.padding(bottom = 16.dp),
                    timeSlots = timeSlotsByDate,
                    selectedTime = uiState.selectedTime,
                    action = action
                )
            }
        }

        // Animated Bottom Sheet
        AnimatedVisibility(
            visible = uiState.selectedTime != null,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BookingBottomSheet(
                modifier = Modifier.fillMaxWidth(),
                expertName = uiState.expertDetails?.firstName + " " + uiState.expertDetails?.lastName,
                formattedDateTime = convertIntoLocalDateTime(
                    date = selectedDate.value?.toKotlinLocalDate(),
                    hr = uiState.selectedTime?.hour ?: 0,
                    min = uiState.selectedTime?.minute ?: 0,
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Available Time Slots",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            FlowRow(
                modifier = Modifier
                    .scrollable(
                        orientation = Orientation.Vertical,
                        state = rememberScrollState(),
                    )
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 3,
            ) {
                timeSlots.forEach { time ->
                    val selected = selectedTime?.let {
                        it.hour == stringToLocalTime(time).hour && it.minute == stringToLocalTime(time).minute
                    } == true
                    DateSlot(
                        modifier = Modifier.fillMaxWidth(0.28f),
                        text = time,
                        selected = selected,
                        enabled = true,
                        onClick = {
                            action(BookingActions.OnTimeSelected(stringToLocalTime(time)))
                        })
                }
            }
        }
    }
}

fun LocalDate?.toPrettyString(): String {
    if (this == null) return ""
    val dayOfWeek = this.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val dayOfMonth = this.dayOfMonth
    val suffix = when {
        dayOfMonth in 11..13 -> "th"
        dayOfMonth % 10 == 1 -> "st"
        dayOfMonth % 10 == 2 -> "nd"
        dayOfMonth % 10 == 3 -> "rd"
        else -> "th"
    }
    val month = this.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
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