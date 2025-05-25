package com.example.talenta.presentation.expertAvailabilitySchedule.ServiceScreen.CreateServiceComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CalendarSection(
    selectedStartDate: Long?,
    selectedEndDate: Long?,
    onDatesSelected: (Long?, Long?) -> Unit
) {
    val primaryBlue = Color(0xFF4A6FFF)
    val lightBlue = Color(0xFFE6EEFF)
    val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

    // Calendar state
    val currentCalendar = Calendar.getInstance()
    val currentYear = currentCalendar.get(Calendar.YEAR)
    val currentMonth = currentCalendar.get(Calendar.MONTH)
    val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)

    var currentMonthOffset by remember { mutableStateOf(0) }
    var displayMonth by remember { mutableStateOf("") }
    val selectedDates = remember { mutableStateListOf<Int>() }

    var currentMonthFirstDay by remember { mutableStateOf(0) }
    var currentMonthTotalDays by remember { mutableStateOf(0) }
    var currentViewYear by remember { mutableStateOf(currentYear) }
    var currentViewMonth by remember { mutableStateOf(currentMonth) }

    // Initialize calendar
    LaunchedEffect(Unit) {
        updateCalendarDisplay(
            0, currentCalendar,
            { year -> currentViewYear = year },
            { month -> currentViewMonth = month },
            { firstDay -> currentMonthFirstDay = firstDay },
            { totalDays -> currentMonthTotalDays = totalDays },
            { display -> displayMonth = display }
        )
    }

    // Month navigation
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = {
            if (currentMonthOffset > 0 ||
                (currentViewYear > currentYear) ||
                (currentViewYear == currentYear && currentViewMonth > currentMonth)) {
                currentMonthOffset -= 1
                updateCalendarDisplay(
                    currentMonthOffset, currentCalendar,
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
            updateCalendarDisplay(
                currentMonthOffset, currentCalendar,
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

    // Day headers
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

    // Calendar grid
    CalendarGrid(
        currentMonthFirstDay = currentMonthFirstDay,
        currentMonthTotalDays = currentMonthTotalDays,
        currentViewYear = currentViewYear,
        currentViewMonth = currentViewMonth,
        currentYear = currentYear,
        currentMonth = currentMonth,
        currentDay = currentDay,
        selectedDates = selectedDates,
        onDatesSelected = onDatesSelected,
        primaryBlue = primaryBlue,
        lightBlue = lightBlue
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Selected date range display
    if (selectedStartDate != null && selectedEndDate != null) {
        val startDateStr = dateFormat.format(Date(selectedStartDate))
        val endDateStr = dateFormat.format(Date(selectedEndDate))
        Text(
            text = "Selected Range: $startDateStr - $endDateStr",
            fontSize = 14.sp,
            color = primaryBlue,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CalendarGrid(
    currentMonthFirstDay: Int,
    currentMonthTotalDays: Int,
    currentViewYear: Int,
    currentViewMonth: Int,
    currentYear: Int,
    currentMonth: Int,
    currentDay: Int,
    selectedDates: MutableList<Int>,
    onDatesSelected: (Long?, Long?) -> Unit,
    primaryBlue: Color,
    lightBlue: Color
) {
    val isInSelectionMode = remember { mutableStateOf(false) }
    var selectionStartDay by remember { mutableStateOf<Int?>(null) }

    for (i in 0 until 6) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (j in 0 until 7) {
                val day = i * 7 + j + 1 - currentMonthFirstDay

                if (day in 1..currentMonthTotalDays) {
                    val isSelected = day in selectedDates
                    val isRangeEndpoint = day == selectedDates.firstOrNull() || day == selectedDates.lastOrNull()

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
                                    val startDay = selectionStartDay!!
                                    selectedDates.clear()

                                    if (startDay <= day) {
                                        for (d in startDay..day) {
                                            selectedDates.add(d)
                                        }
                                    } else {
                                        for (d in day..startDay) {
                                            selectedDates.add(d)
                                        }
                                    }

                                    isInSelectionMode.value = false
                                    selectionStartDay = null

                                    // Update dates
                                    val calendar = Calendar.getInstance()
                                    calendar.set(currentViewYear, currentViewMonth, selectedDates.minOrNull()!!)
                                    val startDate = calendar.timeInMillis

                                    calendar.set(currentViewYear, currentViewMonth, selectedDates.maxOrNull()!!)
                                    val endDate = calendar.timeInMillis

                                    onDatesSelected(startDate, endDate)
                                } else {
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
}

@Composable
fun TimeSelectionSection(
    selectedStartTime: String,
    selectedEndTime: String,
    onStartTimeSelected: (String) -> Unit,
    onEndTimeSelected: (String) -> Unit
) {
    Text(
        text = "Select your timings",
        fontSize = 14.sp,
        color = Color.Gray
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Start Time
        TimeDropdown(
            label = "Start Time",
            selectedTime = selectedStartTime,
            onTimeSelected = onStartTimeSelected,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // End Time
        TimeDropdown(
            label = "End Time",
            selectedTime = selectedEndTime,
            onTimeSelected = onEndTimeSelected,
            includeEndOfDay = true,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TimeDropdown(
    label: String,
    selectedTime: String,
    onTimeSelected: (String) -> Unit,
    includeEndOfDay: Boolean = false,
    modifier: Modifier = Modifier
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label,
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
                .clickable { dropdownExpanded = true }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = selectedTime,
                fontSize = 16.sp
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Time Dropdown",
                tint = Color.Gray,
                modifier = Modifier.align(Alignment.CenterEnd)
            )

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
                modifier = Modifier
                    .heightIn(max = 300.dp)
                    .background(Color.White),
                properties = PopupProperties(focusable = true)
            ) {
                // Generate time options
                for (hour in 0..23) {
                    for (minute in listOf(0, 30)) {
                        val formattedHour = if (hour < 10) "0$hour" else "$hour"
                        val formattedMinute = if (minute == 0) "00" else "$minute"
                        val timeString = "$formattedHour:$formattedMinute"

                        DropdownMenuItem(
                            text = { Text(timeString) },
                            onClick = {
                                onTimeSelected(timeString)
                                dropdownExpanded = false
                            }
                        )
                    }
                }

                // Add 24:00 for end time
                if (includeEndOfDay) {
                    DropdownMenuItem(
                        text = { Text("24:00") },
                        onClick = {
                            onTimeSelected("24:00")
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }
    }
}
// Helper function remains the same
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
    calendar.time = baseCalendar.time
    calendar.add(Calendar.MONTH, monthOffset)

    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    updateDisplay(monthFormat.format(calendar.time))

    updateYear(calendar.get(Calendar.YEAR))
    updateMonth(calendar.get(Calendar.MONTH))
    updateFirstDay((calendar.get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7)
    updateTotalDays(calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
}
