package com.example.talenta.presentation.expertBooking


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talenta.data.model.DateSlot
import com.example.talenta.data.model.ExpertAvailability
import com.example.talenta.data.model.Schedule
import com.example.talenta.data.model.TimeSlot
import com.example.talenta.ui.theme.TalentATheme
import com.example.talenta.utils.HelperFunctions.capitalizeFirstLetter
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.datetime.toJavaLocalDateTime
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Period
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun CustomCalender(
    expertAvailability: ExpertAvailability?,
    modifier: Modifier = Modifier,
    onDateChange: (LocalDate) -> Unit = {}
) {
    val schedule = expertAvailability?.schedule
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth }
    val endMonth = remember { currentMonth.plusMonths(12) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val availableDaysAndMonth = remember(schedule) {
        getAvailableDates(schedule)
    }
    val selectedDay = rememberSaveable(saver = LocalDateSaver) {
        mutableStateOf(LocalDate.now())
    }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    HorizontalCalendar(
        modifier = modifier.fillMaxWidth(),
        state = state,
        contentHeightMode = ContentHeightMode.Wrap,
        calendarScrollPaged = true,
        monthHeader = { calendarMonth ->
            // Month header with gradient background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                            )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = calendarMonth.yearMonth.month.name.capitalizeFirstLetter() + " " + calendarMonth.yearMonth.year,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            val daysOfWeek = calendarMonth.weekDays.first().map { it.date.dayOfWeek }
            EnhancedCalendarHeader(daysOfWeek = daysOfWeek)
        },
        dayContent = { calendarDay ->
            val isAvailable = availableDaysAndMonth.any { pair ->
                pair.first == calendarDay.date.dayOfMonth && pair.second == calendarDay.date.monthValue
            }

            CalendarDayButton(
                text = calendarDay.date.dayOfMonth.toString(),
                isSelected = selectedDay.value == calendarDay.date,
                isEnabled = isAvailable,
                isToday = calendarDay.date == LocalDate.now()
            ) {
                selectedDay.value = calendarDay.date
                onDateChange(calendarDay.date)
            }
        }
    )
}

@Composable
fun CalendarDayButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        isEnabled -> Color.Transparent
        else -> Color.Transparent
    }

    val textColor = when {
        isSelected -> Color.White
        isToday && !isSelected -> MaterialTheme.colorScheme.primary
        isEnabled -> Color.Black
        else -> Color.LightGray
    }

    val borderColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isToday -> MaterialTheme.colorScheme.primary
        isEnabled -> Color.Transparent
        else -> Color.Transparent
    }

    TextButton(
        onClick = onClick,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(
                width = if (isToday && !isSelected) 1.dp else 0.dp,
                color = borderColor,
                shape = CircleShape
            ),
        colors = ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = textColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.LightGray
        ),
        enabled = isEnabled
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isSelected || isToday) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun EnhancedCalendarHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (dayOfWeek in daysOfWeek) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color.Gray.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayOfWeek.displayText(narrow = true),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

fun DayOfWeek.displayText(uppercase: Boolean = false, narrow: Boolean = false): String {
    val style = if (narrow) TextStyle.NARROW else TextStyle.SHORT
    return getDisplayName(style, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

fun getAvailableDates(schedule: List<Schedule>?): List<Pair<Int, Int>> {
    val availableDates = mutableListOf<Pair<Int, Int>>()
    schedule?.forEach { scheduleItem ->
        val startDate = scheduleItem.dateSlot.localStartDateTime()
        val endDate = scheduleItem.dateSlot.localEndDateTime()
        val days = startDate.toJavaLocalDateTime().until(endDate.toJavaLocalDateTime(), ChronoUnit.DAYS)
        val availability = (0..days).map { offset ->
            val currentDate = startDate.toJavaLocalDateTime().plus(Period.ofDays(offset.toInt()))
            currentDate.dayOfMonth to currentDate.monthValue
        }
        availableDates.addAll(availability)
    }
    return availableDates
}

val LocalDateSaver = listSaver(
    save = { listOf(it.value.year, it.value.monthValue, it.value.dayOfMonth) },
    restore = { (year, month, day) -> mutableStateOf(LocalDate.of(year, month, day)) }
)

@Preview(showBackground = true)
@Composable
private fun CalendarPreview() {
    TalentATheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            CustomCalender(
                expertAvailability = ExpertAvailability(
                    timezone = "UTC",
                    schedule = listOf(
                        Schedule(
                            DateSlot(
                                startDateTime = "2023-10-05T00:00:00Z",
                                endDateTime = "2023-12-07T00:00:00Z"
                            ),
                            TimeSlot(
                                start = "15:00",
                                end = "17:00"
                            )
                        )
                    )
                ),
            ) { selectedDate ->
                println("Selected date: $selectedDate")
            }
        }
    }
}