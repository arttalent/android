package com.example.talenta.presentation.expertBooking


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
    expertDetails: User,
    selectedServiceId: String
) {
    val viewModel = hiltViewModel<BookingViewModel>()
    val uiState = viewModel.uiStates.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.onAction(
            BookingActions.InitData(
                expertDetails = expertDetails,
                selectedServiceId = selectedServiceId
            )
        )
    }

    // Listen for successful booking to dismiss bottom sheet
    LaunchedEffect(uiState.isBookingSuccessful) {
        if (uiState.isBookingSuccessful == true) {
            viewModel.onAction(BookingActions.OnTimeSelected(null))
            viewModel.onAction(BookingActions.ResetBookingState)
        }
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8F9FF),
                        Color(0xFFFFFFFF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = if (uiState.selectedTime != null) 120.dp else 16.dp)
        ) {
            // Header Section
            ExpertBookingHeader(
                expertName = "${uiState.expertDetails?.firstName} ${uiState.expertDetails?.lastName}",
                serviceFee = uiState.selectedService?.perHourCharge?.toString() ?: "0"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Calendar Section
            CalendarSection(
                expertAvailability = uiState.selectedService?.expertAvailability,
                onDateSelected = { date ->
                    selectedDate.value = date
                    action(BookingActions.OnDateSelected(date.toKotlinLocalDate()))
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Date and Timezone Info
            SelectedDateInfo(
                selectedDate = selectedDate.value,
                timezone = uiState.selectedService?.expertAvailability?.timezone ?: "UTC"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Time Slots Section
            val timeSlotsByDate = uiState.timeSlotBySelectedDate
            if (timeSlotsByDate.isEmpty()) {
                LaunchedEffect(Unit) {
                    action(BookingActions.OnTimeSelected(null))
                }
                EmptyTimeSlotsCard()
            } else {
                TimeSlotsSection(
                    timeSlots = timeSlotsByDate,
                    selectedTime = uiState.selectedTime,
                    action = action
                )
            }
        }

        // Animated Bottom Sheet
        AnimatedVisibility(
            visible = uiState.selectedTime != null,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300)),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            if (uiState.selectedTime != null) {
                BookingBottomSheet(
                    modifier = Modifier.fillMaxWidth(),
                    expertName = "${uiState.expertDetails?.firstName} ${uiState.expertDetails?.lastName}",
                    time = String.format(
                        "%02d:%02d",
                        uiState.selectedTime.hour,
                        uiState.selectedTime.minute
                    ),
                    date = selectedDate.value.toPrettyString(),
                    fees = "$${uiState.selectedService?.perHourCharge}",
                    loading = uiState.isLoading,
                    onConfirmClick = {
                        action(BookingActions.CreateBooking)
                    },
                    onDismiss = {
                        action(BookingActions.OnTimeSelected(null))
                    }
                )
            }
        }

        // Background overlay when bottom sheet is visible
        val overlayAlpha by animateFloatAsState(
            targetValue = if (uiState.selectedTime != null) 0.3f else 0f,
            animationSpec = tween(300)
        )

        if (overlayAlpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(overlayAlpha)
                    .background(Color.Black)
            )
        }
    }
}

@Composable
fun ExpertBookingHeader(
    expertName: String,
    serviceFee: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Book Session with",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = expertName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$$serviceFee/hour",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun CalendarSection(
    expertAvailability: ExpertAvailability?,
    onDateSelected: (LocalDate) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Select Date",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomCalender(
                expertAvailability = expertAvailability,
                onDateChange = onDateSelected
            )
        }
    }
}

@Composable
fun SelectedDateInfo(
    selectedDate: LocalDate,
    timezone: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "Selected Date",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = selectedDate.toPrettyString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Timezone",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = timezone,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


@Composable
fun TimeSlotsSection(
    timeSlots: List<String>,
    selectedTime: LocalTime?,
    action: (BookingActions) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Available Time Slots",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TimeSlotsRow(
                timeSlots = timeSlots,
                selectedTime = selectedTime,
                action = action
            )
        }
    }
}

@Composable
fun EmptyTimeSlotsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No Time Slots Available",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFE65100),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please select a different date",
                fontSize = 14.sp,
                color = Color(0xFFFF9800),
                textAlign = TextAlign.Center
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
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        maxItemsInEachRow = 3,
    ) {
        timeSlots.forEach { time ->
            val selected = selectedTime?.let {
                it.hour == stringToLocalTime(time).hour && it.minute == stringToLocalTime(time).minute
            } ?: false

            TimeSlot(
                modifier = Modifier.weight(1f),
                text = time,
                selected = selected,
                enabled = true,
                onClick = {
                    action(BookingActions.OnTimeSelected(stringToLocalTime(time)))
                }
            )
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
private fun ExpertBookingScreenPreview() {
    val service = Service(
        serviceId = "1",
        serviceType = ServiceType.VIDEO_ASSESSMENT,
        perHourCharge = 50.04f,
        expertAvailability = ExpertAvailability(
            timezone = "Asia/Kolkata",
            schedule = listOf(
                Schedule(
                    com.example.talenta.data.model.DateSlot(
                        startDateTime = "2023-10-01T00:00:00Z",
                        endDateTime = "2023-11-10T00:00:00Z"
                    ),
                    TimeSlot(
                        start = "15:00",
                        end = "17:00"
                    )
                )
            )
        )
    )

    TalentATheme {
        ExpertBookingScreen(
            uiState = BookingStates(
                expertDetails = User(
                    firstName = "Dr. Sarah",
                    lastName = "Johnson",
                    expertService = listOf(service)
                ),
                selectedDate = LocalDate.now().toKotlinLocalDate(),
                selectedTime = LocalTime(10, 0),
                timeSlotBySelectedDate = listOf("10:00", "11:00", "12:00", "14:00", "15:00", "16:00"),
                isBookingSuccessful = false
            ),
            action = { }
        )
    }
}