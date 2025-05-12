package com.example.talenta.presentation.expertAvailabilitySchedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.talenta.data.model.DayOfWeek
import com.example.talenta.data.model.TimeSlot
import com.example.talenta.ui.theme.TalentATheme


@Composable
fun ExpertAvailabilitySchedule(
    modifier: Modifier = Modifier,
    expertId: String,
) {

    val viewModel =
        hiltViewModel<ExpertAvailabilityScheduleViewModel, ExpertAvailabilityScheduleViewModelFactory>(
            creationCallback = { factory -> factory.create(expertId = expertId) })

    val uiStates = viewModel.uiStates.collectAsState()

    ExpertAvailabilityScheduleScreen(
        expertAvailabilityStates = uiStates.value, onAction = viewModel::onAction
    ){

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpertAvailabilityScheduleScreen(
    expertAvailabilityStates: ExpertAvailabilityScheduleState,
    onAction: (ExpertAvailabilityScheduleActions) -> Unit,
    onBackClick: () -> Unit = {},
) {

    var selectedDay by remember { mutableStateOf<DayOfWeek>(DayOfWeek.SUN) }
    var startTime by remember { mutableStateOf("00:00") }
    var endTime by remember { mutableStateOf("24:00") }

    LaunchedEffect(
        selectedDay
    ) {
        onAction(ExpertAvailabilityScheduleActions.OnWeekdayDaySelect(selectedDay))
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Schedule") }, navigationIcon = {
                IconButton(onClick = { /* Handle navigation back */ }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })
        }) { paddingValues ->
        Box {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
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
                            text = "Select day", fontSize = 18.sp, fontWeight = FontWeight.Medium
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
                            DayOfWeek.entries.forEach { day ->
                                val isSelected = selectedDay == day
                                DayButton(
                                    day = day, isSelected = isSelected, onClick = {
                                        selectedDay = day
                                    })
                            }
                        }
                    }
                }

                // Set Your Availability section
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
                                onTimeSelected = { startTime = it })

                            // End time dropdown
                            TimeDropdown(
                                time = endTime,
                                modifier = Modifier.weight(1f),
                                onTimeSelected = { endTime = it })
                        }

                        // Save button
                        Button(
                            onClick = {
                                // Todo Add TimeSlots Button
                                /*if (startTime < endTime) {
                                    // Add new time slot with day information
                                    timeSlots = timeSlots + TimeSlot(selectedDay, startTime, endTime)
                                }*/
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Add Time Slot")
                        }
                    }
                }

                // Saved time slots
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

                        LazyColumn(
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            items(items = expertAvailabilityStates.selectedDayAvailabilitySlot) { timeSlot ->
                                SavedTimeSlotItem(
                                    modifier = Modifier.fillMaxWidth(), timeSlot = timeSlot
                                )
                            }
                            item {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                )
                            }

                        }


                        // Add divider except for the last item
                        /*if (index < timeSlots.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                thickness = 2.dp, color = Color.LightGray
                            )
                        }*/
                    }
                }
            }


            // Save button
            Button(
                onClick = {
                    // Todo Save the whole Schedule Button
                    /*if (startTime < endTime) {
                        // Add new time slot with day information
                        timeSlots = timeSlots + TimeSlot(selectedDay, startTime, endTime)
                    }*/
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save Schedule")
            }
        }
    }
}

@Composable
fun SavedTimeSlotItem(modifier: Modifier = Modifier, timeSlot: TimeSlot) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "${timeSlot.start} - ${timeSlot.end}", fontSize = 16.sp
            )
        }

        IconButton(
            onClick = {
                // Todo Remove time slot
            }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove time slot",
                tint = Color.Red
            )
        }
    }
}

@Composable
fun DayButton(
    day: DayOfWeek, isSelected: Boolean, onClick: () -> Unit
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
            containerColor = backgroundColor, contentColor = contentColor
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
            }, fontSize = 14.sp
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
    time: String, modifier: Modifier = Modifier, onTimeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Sample time options (simplified for demonstration)
    val timeOptions = (0..23).map { hour -> "%02d:00".format(hour) }

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
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight(0.5f)
        ) {
            timeOptions.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onTimeSelected(option)
                    expanded = false
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
    TalentATheme {
        ExpertAvailabilityScheduleScreen(
            expertAvailabilityStates = ExpertAvailabilityScheduleState(
                expertAvailability = null,
                selectedDay = DayOfWeek.MON,
                selectedDayAvailabilitySlot = listOf(
                    TimeSlot("08:00", "10:00"),
                    TimeSlot("12:00", "14:00"),
                    TimeSlot("12:00", "14:00"),
                    TimeSlot("12:00", "14:00"),
                    TimeSlot("12:00", "14:00"),
                    TimeSlot("12:00", "14:00"),
                ),
                isLoading = false,
                errorMessage = null
            ), onAction = {}) {

        }
    }
}
