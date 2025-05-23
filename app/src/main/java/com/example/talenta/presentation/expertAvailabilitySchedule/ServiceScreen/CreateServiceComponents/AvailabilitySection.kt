package com.example.talenta.presentation.expertAvailabilitySchedule.ServiceScreen.CreateServiceComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AvailabilitySection(
    selectedStartDate: Long?,
    selectedEndDate: Long?,
    selectedStartTime: String,
    selectedEndTime: String,
    onDatesSelected: (Long?, Long?) -> Unit,
    onStartTimeSelected: (String) -> Unit,
    onEndTimeSelected: (String) -> Unit
) {
    Text(
        text = "Set your availability",
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

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
            // Calendar Section
            CalendarSection(
                selectedStartDate = selectedStartDate,
                selectedEndDate = selectedEndDate,
                onDatesSelected = onDatesSelected
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Time Selection Section
            TimeSelectionSection(
                selectedStartTime = selectedStartTime,
                selectedEndTime = selectedEndTime,
                onStartTimeSelected = onStartTimeSelected,
                onEndTimeSelected = onEndTimeSelected
            )
        }
    }
}