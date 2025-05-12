package com.example.talenta.presentation.expertBooking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talenta.ui.theme.TalentATheme
import com.example.talenta.utils.HelperFunctions.capitalizeFirstLetter
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun CustomCalender(modifier: Modifier = Modifier, onDateChange: (LocalDate) -> Unit = {}) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(1) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val availableDaysAndMonth = remember {
        getNextTwoWeeksDates()
    }
    var selectedDay = rememberSaveable(saver = LocalDateSaver) {
        mutableStateOf(LocalDate.now())
    }


    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    HorizontalCalendar(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        state = state,
        contentHeightMode = ContentHeightMode.Wrap,
        calendarScrollPaged = true,
        monthHeader = { it ->
            Text(
                text = it.yearMonth.month.name.capitalizeFirstLetter() + " " + it.yearMonth.year,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
            val daysOfWeek = it.weekDays.first().map { it.date.dayOfWeek }
            CalendarHeader(daysOfWeek = daysOfWeek)
        },
        dayContent = { it ->
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

@Composable
fun CalendarDayButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
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
            .clip(CircleShape)
            .padding(horizontal = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor, contentColor = textColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.LightGray
        ),
    ) {
        Text(
            text = text, modifier = Modifier
        )
    }

}

@Composable
fun CalendarHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                text = dayOfWeek.displayText(),
            )
        }
    }
}

fun DayOfWeek.displayText(uppercase: Boolean = false, narrow: Boolean = false): String {
    val style = if (narrow) TextStyle.NARROW else TextStyle.SHORT
    return getDisplayName(style, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

fun getNextTwoWeeksDates(): List<Pair<Int, Int>> {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return (0..13).map { daysToAdd ->
        val nextDate = today.plus(value = daysToAdd, unit = DateTimeUnit.DAY)
        nextDate.dayOfMonth to nextDate.monthNumber
    }
}


val LocalDateSaver = listSaver(
    save = {
        listOf(it.value.year, it.value.monthValue, it.value.dayOfMonth)
    },
    restore = { (year, month, day) -> mutableStateOf(LocalDate.of(year, month, day)) }
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
            CustomCalender() {
                // Handle date change
                println("Selected date: $it")
            }
        }
    }
}