package com.example.talenta.presentation.expertBooking

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.remember
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
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpertBookingsScreen(modifier: Modifier = Modifier) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(1) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library

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
                fontSize = 20.sp,
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
                isSelected = true,
            ) {

            }
            //  Text(text = it.date.dayOfMonth.toString())
        })
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ExpertBookingScreenPreview() {
    TalentATheme {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ExpertBookingsScreen()
            }

        }
    }
}

@Composable
fun CalendarDayButton(
    modifier: Modifier = Modifier, text: String, isSelected: Boolean, onClick: () -> Unit
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
            containerColor = backgroundColor, contentColor = textColor
        ),
    ) {
        Text(
            text = text, modifier = Modifier
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
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
                fontSize = 15.sp,
                text = dayOfWeek.displayText(),
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun DayOfWeek.displayText(uppercase: Boolean = false, narrow: Boolean = false): String {
    val style = if (narrow) TextStyle.NARROW else TextStyle.SHORT
    return getDisplayName(style, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}