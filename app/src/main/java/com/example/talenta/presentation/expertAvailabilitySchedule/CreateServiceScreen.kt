package com.example.talenta.presentation.expertAvailabilitySchedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.talenta.data.model.ServiceType
import com.example.talenta.data.model.getTitle
import com.example.talenta.ui.theme.TalentATheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateServiceScreen() {
    val primaryBlue = Color(0xFF4A6FFF)
    val lightBlue = Color(0xFFE6EEFF)
    val scrollState = rememberScrollState()

    // List of service types
    val serviceTypes = ServiceType.entries.map {
        it.getTitle()
    }

    var selectedServiceType by remember { mutableStateOf("") }
    var serviceDropdownExpanded by remember { mutableStateOf(false) }
    var hourlyPay by remember { mutableStateOf("") }
    var additionalNotes by remember { mutableStateOf("") }
    var selectedStartTime by remember { mutableStateOf("00:00") }
    var selectedEndTime by remember { mutableStateOf("24:00") }
    var startTimeDropdownExpanded by remember { mutableStateOf(false) }
    var endTimeDropdownExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Date range states
    var selectedStartDate by rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedEndDate by rememberSaveable { mutableStateOf<Long?>(null) }
    val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

    // Get current date information for disabling past dates
    val currentCalendar = Calendar.getInstance()
    val currentYear = currentCalendar.get(Calendar.YEAR)
    val currentMonth = currentCalendar.get(Calendar.MONTH)
    val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)

    // For display in the UI - Start with current month and year
    var currentMonthOffset by remember { mutableStateOf(0) }
    var displayMonth by remember { mutableStateOf("") }
    val selectedDates = remember { mutableStateListOf<Int>() }

    // Initialize calendar variables
    var currentMonthFirstDay by remember { mutableStateOf(0) }
    var currentMonthTotalDays by remember { mutableStateOf(0) }
    var currentViewYear by remember { mutableStateOf(currentYear) }
    var currentViewMonth by remember { mutableStateOf(currentMonth) }

    // Initialize month display
    LaunchedEffect(Unit) {
        updateCalendarDisplay(0, currentCalendar,
            { year -> currentViewYear = year },
            { month -> currentViewMonth = month },
            { firstDay -> currentMonthFirstDay = firstDay },
            { totalDays -> currentMonthTotalDays = totalDays },
            { display -> displayMonth = display }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Create Service", fontWeight = FontWeight.Medium) },
            navigationIcon = {
                IconButton(onClick = { /* Handle back navigation */ }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black
            )
        )

        // Content - Now with vertical scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Type of service
            Text(
                text = "Type of service",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Service Type Dropdown
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { serviceDropdownExpanded = true }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (selectedServiceType.isEmpty()) "Select the type of service" else selectedServiceType,
                    color = if (selectedServiceType.isEmpty()) Color.Gray else Color.Black
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )

                // Dropdown menu
                DropdownMenu(
                    expanded = serviceDropdownExpanded,
                    onDismissRequest = { serviceDropdownExpanded = false },
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .background(Color.White),
                    properties = PopupProperties(focusable = true)
                ) {
                    serviceTypes.forEach { service ->
                        DropdownMenuItem(
                            text = { Text(service) },
                            onClick = {
                                selectedServiceType = service
                                serviceDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hourly pay
            Text(
                text = "Hourly pay",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Hourly pay field
            TextField(
                value = hourlyPay,
                onValueChange = { hourlyPay = it },
                placeholder = { Text("Enter the cost to be paid per hour") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Set your availability
            Text(
                text = "Set your availability",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Calendar container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    // Month header with arrows
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = {
                            // Only allow going back if we're not already in the current month
                            if (currentMonthOffset > 0 ||
                                (currentViewYear > currentYear) ||
                                (currentViewYear == currentYear && currentViewMonth > currentMonth)) {
                                currentMonthOffset -= 1
                                updateCalendarDisplay(currentMonthOffset, currentCalendar,
                                    { year -> currentViewYear = year },
                                    { month -> currentViewMonth = month },
                                    { firstDay -> currentMonthFirstDay = firstDay },
                                    { totalDays -> currentMonthTotalDays = totalDays },
                                    { display -> displayMonth = display }
                                )
                            }
                        }) {
                            Text("◀", fontSize = 18.sp)
                        }
                        Text(
                            text = displayMonth,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                        IconButton(onClick = {
                            currentMonthOffset += 1
                            updateCalendarDisplay(currentMonthOffset, currentCalendar,
                                { year -> currentViewYear = year },
                                { month -> currentViewMonth = month },
                                { firstDay -> currentMonthFirstDay = firstDay },
                                { totalDays -> currentMonthTotalDays = totalDays },
                                { display -> displayMonth = display }
                            )
                        }) {
                            Text("▶", fontSize = 18.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Day of week headers
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val daysOfWeek = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
                        for (day in daysOfWeek) {
                            Text(
                                text = day,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Calendar grid with clickable dates
                    val isInSelectionMode = remember { mutableStateOf(false) }
                    var selectionStartDay by remember { mutableStateOf<Int?>(null) }

                    for (i in 0 until 6) { // Support up to 6 weeks in month view
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (j in 0 until 7) { // 7 days in a week
                                val day = i * 7 + j + 1 - currentMonthFirstDay

                                if (day in 1..currentMonthTotalDays) {
                                    val isSelected = day in selectedDates
                                    val isRangeEndpoint = day == selectedDates.firstOrNull() || day == selectedDates.lastOrNull()

                                    // Check if date is in the past
                                    val isPastDate = (currentViewYear < currentYear) ||
                                            (currentViewYear == currentYear && currentViewMonth < currentMonth) ||
                                            (currentViewYear == currentYear && currentViewMonth == currentMonth && day < currentDay)

                                    val dateColor = when {
                                        isPastDate -> Color.LightGray
                                        isRangeEndpoint -> primaryBlue
                                        isSelected -> lightBlue
                                        else -> Color.White
                                    }

                                    val textColor = when {
                                        isPastDate -> Color.Gray
                                        isRangeEndpoint -> Color.White
                                        else -> Color.Black
                                    }

                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(dateColor)
                                            .clickable(enabled = !isPastDate) {
                                                if (isInSelectionMode.value) {
                                                    // Complete the selection
                                                    val startDay = selectionStartDay!!
                                                    selectedDates.clear()

                                                    if (startDay <= day) {
                                                        // Forward selection
                                                        for (d in startDay..day) {
                                                            selectedDates.add(d)
                                                        }
                                                    } else {
                                                        // Backward selection
                                                        for (d in day..startDay) {
                                                            selectedDates.add(d)
                                                        }
                                                    }

                                                    isInSelectionMode.value = false
                                                    selectionStartDay = null

                                                    // Update selectedStartDate and selectedEndDate
                                                    val calendar = Calendar.getInstance()
                                                    calendar.set(
                                                        currentViewYear,
                                                        currentViewMonth,
                                                        selectedDates.minOrNull()!!
                                                    )
                                                    selectedStartDate = calendar.timeInMillis

                                                    calendar.set(
                                                        currentViewYear,
                                                        currentViewMonth,
                                                        selectedDates.maxOrNull()!!
                                                    )
                                                    selectedEndDate = calendar.timeInMillis

                                                } else {
                                                    // Start a new selection
                                                    selectedDates.clear()
                                                    selectedDates.add(day)
                                                    selectionStartDay = day
                                                    isInSelectionMode.value = true
                                                }
                                            }
                                    ) {
                                        Text(
                                            text = day.toString(),
                                            fontSize = 14.sp,
                                            color = textColor,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                } else {
                                    Box(modifier = Modifier.size(36.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Selected date range display
                    if (selectedStartDate != null && selectedEndDate != null) {
                        val startDateStr = dateFormat.format(Date(selectedStartDate!!))
                        val endDateStr = dateFormat.format(Date(selectedEndDate!!))
                        Text(
                            text = "Selected Range: $startDateStr - $endDateStr",
                            fontSize = 14.sp,
                            color = primaryBlue,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Select your timings
                    Text(
                        text = "Select your timings",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Time selection with dropdowns
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Start time selection
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Start Time",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color.LightGray,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { startTimeDropdownExpanded = true }
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = selectedStartTime,
                                    fontSize = 16.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Time Dropdown",
                                    tint = Color.Gray,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )

                                // Time dropdown menu
                                DropdownMenu(
                                    expanded = startTimeDropdownExpanded,
                                    onDismissRequest = { startTimeDropdownExpanded = false },
                                    modifier = Modifier
                                        .heightIn(max = 300.dp)
                                        .background(Color.White),
                                    properties = PopupProperties(focusable = true)
                                ) {
                                    // Generate all 24-hour options in 30-minute intervals
                                    for (hour in 0..23) {
                                        for (minute in listOf(0, 30)) {
                                            val formattedHour = if (hour < 10) "0$hour" else "$hour"
                                            val formattedMinute = if (minute == 0) "00" else "$minute"
                                            val timeString = "$formattedHour:$formattedMinute"

                                            DropdownMenuItem(
                                                text = { Text(timeString) },
                                                onClick = {
                                                    selectedStartTime = timeString
                                                    startTimeDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // End time selection
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "End Time",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color.LightGray,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { endTimeDropdownExpanded = true }
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = selectedEndTime,
                                    fontSize = 16.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Time Dropdown",
                                    tint = Color.Gray,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )

                                // Time dropdown menu
                                DropdownMenu(
                                    expanded = endTimeDropdownExpanded,
                                    onDismissRequest = { endTimeDropdownExpanded = false },
                                    modifier = Modifier
                                        .heightIn(max = 300.dp)
                                        .background(Color.White),
                                    properties = PopupProperties(focusable = true)
                                ) {
                                    // Generate all 24-hour options in 30-minute intervals
                                    for (hour in 0..23) {
                                        for (minute in listOf(0, 30)) {
                                            val formattedHour = if (hour < 10) "0$hour" else "$hour"
                                            val formattedMinute = if (minute == 0) "00" else "$minute"
                                            val timeString = "$formattedHour:$formattedMinute"

                                            DropdownMenuItem(
                                                text = { Text(timeString) },
                                                onClick = {
                                                    selectedEndTime = timeString
                                                    endTimeDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }

                                    // Add 24:00 as the last option
                                    DropdownMenuItem(
                                        text = { Text("24:00") },
                                        onClick = {
                                            selectedEndTime = "24:00"
                                            endTimeDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Next button
            Button(
                onClick = { /* Handle next action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save")
            }

            // Add some space at the bottom for better scrolling
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
private fun CreateServiceScreenPrev() {
    TalentATheme {
        CreateServiceScreen()
    }
}

// Helper function to update calendar display
private fun updateCalendarDisplay(
    monthOffset: Int,
    baseCalendar: Calendar,
    updateYear: (Int) -> Unit,
    updateMonth: (Int) -> Unit,
    updateFirstDay: (Int) -> Unit,
    updateTotalDays: (Int) -> Unit,
    updateDisplay: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    // Start with the current date
    calendar.time = baseCalendar.time
    calendar.add(Calendar.MONTH, monthOffset)

    // Update month display
    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    updateDisplay(monthFormat.format(calendar.time))

    // Update month details
    updateYear(calendar.get(Calendar.YEAR))
    updateMonth(calendar.get(Calendar.MONTH))
    updateFirstDay((calendar.get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7) // Adjust for Monday as first day
    updateTotalDays(calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
}

