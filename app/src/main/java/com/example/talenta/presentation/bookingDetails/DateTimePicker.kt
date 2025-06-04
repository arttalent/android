
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toLocalDateTime
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTime2Selector(
    onDateTimeSelected: (LocalDateTime?) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }

    // Only allow dates from today onwards (kotlinx-datetime)
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { showDatePicker = true }
        ) {
            Text(
                text = selectedDate?.let { "Date: ${it}" } ?: "Select Date"
            )
        }
        if (selectedDate != null) {
            if (showTimePicker) {
                val currentTime = java.util.Calendar.getInstance()
                val timePickerState = rememberTimePickerState(
                    initialHour = currentTime.get(java.util.Calendar.HOUR_OF_DAY),
                    initialMinute = currentTime.get(java.util.Calendar.MINUTE),
                    is24Hour = true
                )
                Column {
                    TimePicker(
                        state = timePickerState,
                        modifier = Modifier.padding(8.dp)
                    )
                    Row(Modifier.padding(top = 8.dp)) {
                        Button(onClick = { showTimePicker = false }) {
                            Text("Cancel")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = {
                            selectedTime = LocalTime(timePickerState.hour, timePickerState.minute)
                            showTimePicker = false
                        }) {
                            Text("OK")
                        }
                    }
                }
            } else {
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { showTimePicker = true }
                ) {
                    Text(
                        text = selectedTime?.let { "Time: ${it}" } ?: "Select Time"
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        if (selectedDate != null && selectedTime != null) {
            val dateTime = selectedDate!!.atTime(selectedTime!!)
            Text("Selected: ${dateTime}")
            Button(onClick = { onDateTimeSelected(dateTime) }) {
                Text("Confirm")
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val date = Instant.ofEpochMilli(utcTimeMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    val kotlinxDate = LocalDate(date.year, date.monthValue, date.dayOfMonth)
                    return kotlinxDate >= today
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            selectedDate = LocalDate(date.year, date.monthValue, date.dayOfMonth)
                            showDatePicker = false
                            showTimePicker = true
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview
@Composable
fun LocalDateTimeS(modifier: Modifier = Modifier) {
    DateTime2Selector {

    }
}
