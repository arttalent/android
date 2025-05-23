package com.example.talenta.presentation.expertAvailabilitySchedule.ServiceScreen.CreateServiceComponents

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HourlyPaySection(
    hourlyPay: String,
    onHourlyPayChanged: (String) -> Unit
) {
    Text(
        text = "Hourly pay",
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    TextField(
        value = hourlyPay,
        onValueChange = onHourlyPayChanged,
        placeholder = { Text("Enter the cost to be paid per hour") },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.LightGray,
            unfocusedIndicatorColor = Color.LightGray
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true
    )
}