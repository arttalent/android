package com.example.talenta.presentation.expertBooking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
    val startMonth = remember { currentMonth } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val availableDaysAndMonth = remember(schedule) {
        getAvailableDates(schedule)
    }
    val selectedDay = rememberSaveable(saver = LocalDateSaver) {
        mutableStateOf(
            LocalDate.now()
        )
    }
    LaunchedEffect(key1 = availableDaysAndMonth) {
        if (availableDaysAndMonth.isNotEmpty()) {
            selectedDay.value = LocalDate.of(
                currentMonth.year,
                availableDaysAndMonth.first().second,
                availableDaysAndMonth.first().first
            )
            selectedDay.value?.let {
                onDateChange(it)
            }
        }

    }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        HorizontalCalendar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            state = state,
            contentHeightMode = ContentHeightMode.Wrap,
            calendarScrollPaged = true,
            monthHeader = { it ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = it.yearMonth.month.name.capitalizeFirstLetter() + " " + it.yearMonth.year,
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                val daysOfWeek = it.weekDays.first().map { it.date.dayOfWeek }
                CalendarHeader(daysOfWeek = daysOfWeek)
            },
            dayContent = {
                CalendarDayButton(
                    text = it.date.dayOfMonth.toString(),
                    modifier = Modifier,
                    isSelected = selectedDay.value == it.date,
                    isEnabled = availableDaysAndMonth.any { pair ->
                        pair.first == it.date.dayOfMonth && pair.second == it.date.monthValue
                    },
                ) {
                    selectedDay.value = it.date
                    onDateChange(it.date)
                }
            })
    }
}

@Composable
fun CalendarDayButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            )
        )

        isEnabled -> Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color.Transparent
            )
        )

        else -> Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color.Transparent
            )
        )
    }

    val textColor = when {
        isSelected -> Color.White
        isEnabled -> MaterialTheme.colorScheme.onSurface
        else -> Color.LightGray
    }
    Box(
        modifier = modifier
            .padding(2.dp)
    ) {
        TextButton(
            onClick = onClick,
            modifier = Modifier
                .clip(CircleShape)
                .background(backgroundColor)
                .then(
                    if (isSelected) {
                        Modifier.shadow(4.dp, CircleShape)
                    } else Modifier
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
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun CalendarHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
    ) {
        for (dayOfWeek in daysOfWeek) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    text = dayOfWeek.displayText().uppercase(),
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
    schedule?.forEach {
        val startDate = it.dateSlot.localStartDateTime()
        val endDate = it.dateSlot.localEndDateTime()
        val days =
            startDate.toJavaLocalDateTime().until(endDate.toJavaLocalDateTime(), ChronoUnit.DAYS)
        val availability = (0..days).map { offset ->
            val currentDate = startDate.toJavaLocalDateTime().plus(
                Period.ofDays(offset.toInt())
            )
            currentDate.dayOfMonth to currentDate.monthValue
        }
        availableDates.addAll(availability)
    }
    return availableDates
}

val LocalDateSaver = listSaver<MutableState<LocalDate?>, Any>(
    save = { state ->
        state.value?.let { localDate ->
            listOf(localDate.year, localDate.monthValue, localDate.dayOfMonth)
        } ?: emptyList()
    },
    restore = { list ->
        if (list.isNotEmpty()) {
            mutableStateOf(
                LocalDate.of(
                    list[0] as Int,
                    list[1] as Int,
                    list[2] as Int
                )
            )
        } else {
            mutableStateOf(null)
        }
    }
)

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun CustomCalendarScreenPreview() {
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
                                endDateTime = "2023-15-07T00:00:00Z"
                            ), TimeSlot(
                                start = "15:00",
                                end = "17:00"
                            )
                        )
                    )
                ),
            ) {
                // Handle date change
                println("Selected date: $it")
            }
        }
    }
}