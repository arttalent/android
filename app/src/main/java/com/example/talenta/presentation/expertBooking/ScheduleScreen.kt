package com.example.talenta.presentation.expertBooking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talenta.data.model.DayOfWeek
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen() {

    var selectedDay by remember { mutableStateOf<DayOfWeek?>(DayOfWeek.SUN) }
    var timeSlots by remember { mutableStateOf(listOf<TimeSlot>()) }
    var startTime by remember { mutableStateOf("09:00") }
    var endTime by remember { mutableStateOf("12:00") }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Schedule") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle navigation back */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Select days section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Select day",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "Select the day you are available",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DayOfWeek.values().forEach { day ->
                            val isSelected = selectedDay == day
                            DayButton(
                                day = day,
                                isSelected = isSelected,
                                onClick = {
                                    // Set the selected day (only one can be selected)
                                    selectedDay = day
                                }
                            )
                        }
                    }
                }
            }

            // Set availability times section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Set your availability",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )

                        // Add plus button for adding time slots
                        IconButton(
                            onClick = {
                                if (startTime < endTime && selectedDay != null) {
                                    // Add new time slot with day information
                                    timeSlots = timeSlots + TimeSlot(selectedDay!!, startTime, endTime)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add time slot",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Text(
                        text = "Select the time slots you are available",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Start time dropdown
                        TimeDropdown(
                            time = startTime,
                            modifier = Modifier.weight(1f),
                            onTimeSelected = { startTime = it }
                        )

                        // End time dropdown
                        TimeDropdown(
                            time = endTime,
                            modifier = Modifier.weight(1f),
                            onTimeSelected = { endTime = it }
                        )
                    }

                    // Save button
                    Button(
                        onClick = {
                            if (startTime < endTime && selectedDay != null) {
                                // Add new time slot with day information
                                timeSlots = timeSlots + TimeSlot(selectedDay!!, startTime, endTime)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save Time Slot")
                    }
                }
            }

            // Display saved time slots
            if (timeSlots.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Saved Time Slots",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        timeSlots.forEachIndexed { index, timeSlot ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = getDayName(timeSlot.day),
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "${timeSlot.startTime} - ${timeSlot.endTime}",
                                        fontSize = 16.sp
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        // Remove time slot
                                        timeSlots = timeSlots.toMutableList().apply {
                                            removeAt(index)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove time slot",
                                        tint = Color.Red
                                    )
                                }
                            }

                            // Add divider except for the last item
                            if (index < timeSlots.size - 1) {
                                Divider(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    color = Color.LightGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Data class to store time slots with day information
data class TimeSlot(
    val day: DayOfWeek,
    val startTime: String,
    val endTime: String
)

@Composable
fun DayButton(
    day: DayOfWeek,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val contentColor = if (isSelected) Color.White else Color.DarkGray
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.size(40.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = when (day) {
                DayOfWeek.MON -> "M"
                DayOfWeek.TUE -> "T"
                DayOfWeek.WED -> "W"
                DayOfWeek.THU -> "Th"
                DayOfWeek.FRI -> "F"
                DayOfWeek.SAT -> "Sa"
                DayOfWeek.SUN -> "Su"
            },
            fontSize = 14.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
// Helper function to get full day name
fun getDayName(day: DayOfWeek): String {
    return when (day) {
        DayOfWeek.MON -> "Monday"
        DayOfWeek.TUE -> "Tuesday"
        DayOfWeek.WED -> "Wednesday"
        DayOfWeek.THU -> "Thursday"
        DayOfWeek.FRI -> "Friday"
        DayOfWeek.SAT -> "Saturday"
        DayOfWeek.SUN -> "Sunday"
    }
}

@Composable
fun TimeDropdown(
    time: String,
    modifier: Modifier = Modifier,
    onTimeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Sample time options (simplified for demonstration)
    val timeOptions = listOf(
        "09:00", "10:00", "11:00", "12:00", "13:00", "14:00",
        "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"
    )

    Box(modifier = modifier) {
        OutlinedTextField(
            value = time,
            onValueChange = { },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select time"
                    )
                }
            },
            shape = RoundedCornerShape(8.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(200.dp)
        ) {
            timeOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onTimeSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
    MaterialTheme {
        ScheduleScreen()
    }
}
