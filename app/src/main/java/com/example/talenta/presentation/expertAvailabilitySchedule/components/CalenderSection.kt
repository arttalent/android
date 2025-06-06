package com.example.talenta.presentation.expertAvailabilitySchedule.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.talenta.presentation.expertBooking.CalendarHeader
import com.example.talenta.presentation.expertBooking.LocalDateSaver
import com.example.talenta.utils.HelperFunctions.capitalizeFirstLetter
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun CalendarSection(
    onDatesSelected: (LocalDate?, LocalDate?) -> Unit
) {
    val selectedStartDay = rememberSaveable(saver = LocalDateSaver) {
        mutableStateOf<LocalDate?>(null)
    }
    val selectedEndDay = rememberSaveable(saver = LocalDateSaver) {
        mutableStateOf<LocalDate?>(null)
    }

    Column {
        Spacer(modifier = Modifier.height(8.dp))

        // Calendar grid
        CalendarGrid { selectedStartDay, selectedEndDay ->
            onDatesSelected(selectedStartDay, selectedEndDay)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Selected date range display
        if (selectedStartDay.value != null && selectedEndDay.value != null) {
            Text(
                text = selectedStartDay.value.toPrettyString() + "-" + selectedEndDay.value.toPrettyString(),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

fun LocalDate?.toPrettyString(): String {
    if (this == null) return ""
    val day = this.dayOfMonth
    val dayWithSuffix = when {
        day in 11..13 -> "${day}th"
        day % 10 == 1 -> "${day}st"
        day % 10 == 2 -> "${day}nd"
        day % 10 == 3 -> "${day}rd"
        else -> "${day}th"
    }

    val formatter = DateTimeFormatter.ofPattern("MMM yyyy")
    val monthYear = this.format(formatter)

    return "$dayWithSuffix $monthYear"
}

@Composable
fun CalendarGrid(
    modifier: Modifier = Modifier, onDatesSelected: (LocalDate?, LocalDate?) -> Unit
) {

    var currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val selectedStartDay = rememberSaveable(saver = LocalDateSaver) {
        mutableStateOf<LocalDate?>(null)
    }
    val selectedEndDay = rememberSaveable(saver = LocalDateSaver) {
        mutableStateOf<LocalDate?>(null)
    }
    LaunchedEffect(selectedEndDay.value, selectedStartDay.value) {
        onDatesSelected(selectedStartDay.value, selectedEndDay.value)
    }
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek,
    )
    val scope = rememberCoroutineScope()

    HorizontalCalendar(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        state = state,
        contentHeightMode = ContentHeightMode.Wrap,
        calendarScrollPaged = true,
        monthHeader = { it ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    scope.launch {
                        state.animateScrollToMonth(currentMonth.minusMonths(1))
                        currentMonth = currentMonth.minusMonths(1)
                    }
                }) {
                    Text("◀", fontSize = 18.sp)
                }
                Text(
                    text = it.yearMonth.month.name.capitalizeFirstLetter() + " " + it.yearMonth.year,
                    modifier = Modifier
                        .padding(8.dp),
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = {
                    scope.launch {
                        state.animateScrollToMonth(currentMonth.plusMonths(1))
                        currentMonth = currentMonth.plusMonths(1)
                    }
                }) {
                    Text("▶", fontSize = 18.sp)
                }
            }
            val daysOfWeek = it.weekDays.first().map { it.date.dayOfWeek }
            CalendarHeader(daysOfWeek = daysOfWeek)
        },
        dayContent = {
            val isDateOlderThanToday = it.date.isAfter(LocalDate.now())
            val isSelectedDate =
                selectedStartDay.value == it.date || selectedEndDay.value == it.date
            val isInRange = if (selectedStartDay.value != null && selectedEndDay.value != null) {
                it.date.isAfter(selectedStartDay.value!!) && it.date.isBefore(selectedEndDay.value!!)
            } else {
                false
            }
            DayButton(
                text = it.date.dayOfMonth.toString(),
                modifier = Modifier,
                isSelected = isSelectedDate,
                isInRange = isInRange,
                isEnabled = isDateOlderThanToday,
            ) {
                when {
                    selectedStartDay.value == null -> {
                        selectedStartDay.value = it.date
                    }

                    selectedStartDay.value != null && selectedEndDay.value == null -> {
                        selectedEndDay.value = it.date
                    }

                    selectedStartDay.value != null && selectedEndDay.value != null -> {
                        // Reset selection if both dates are already selected
                        selectedStartDay.value = it.date
                        selectedEndDay.value = null
                    }

                    else -> {

                    }
                }
            }
        })
}

@Composable
fun DayButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    isInRange: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else if (isInRange) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
    } else {
        Color.Transparent
    }

    val textColor = if (isSelected) {
        Color.White
    } else {
        Color.Black
    }
    TextButton(
        onClick = onClick,
        modifier = modifier
            .run {
                if (isInRange) {
                    this
                } else {
                    this.clip(CircleShape)
                }
            }
            .padding(horizontal = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor, contentColor = textColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.LightGray
        ),
        enabled = isEnabled
    ) {
        Text(
            text = text, modifier = Modifier
        )
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
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TimeDropdown(
    label: String,
    selectedTime: String,
    onTimeSelected: (String) -> Unit,
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
                    for (minute in listOf(0)) {
                        val formattedHour = if (hour < 10) "0$hour" else "$hour"
                        val timeString = "$formattedHour:00"

                        DropdownMenuItem(
                            text = { Text(timeString) },
                            onClick = {
                                onTimeSelected(timeString)
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CalendarSectionPrev() {
    CalendarSection(
        onDatesSelected = { start, end ->
            println("Selected Start: $start, End: $end")
        }
    )
}

