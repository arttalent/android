package com.example.talenta.presentation.expertAvailabilitySchedule.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talenta.presentation.expertAvailabilitySchedule.CreateServiceUiState
import com.example.talenta.presentation.expertAvailabilitySchedule.CreateServiceViewModel
import com.example.talenta.presentation.expertAvailabilitySchedule.SaveButton

@Composable
fun CreateServiceContent(
    uiState: CreateServiceUiState,
    viewModel: CreateServiceViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Service Type Section
        ServiceTypeSection(
            selectedServiceType = uiState.selectedServiceType,
            onServiceTypeSelected = viewModel::updateServiceType
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hourly Pay Section
        HourlyPaySection(
            hourlyPay = uiState.hourlyPay,
            onHourlyPayChanged = viewModel::updateHourlyPay
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Availability Section
        AvailabilitySection(
            selectedStartTime = uiState.selectedStartTime,
            selectedEndTime = uiState.selectedEndTime,
            onDatesSelected = viewModel::updateSelectedDates,
            onStartTimeSelected = viewModel::updateStartTime,
            onEndTimeSelected = viewModel::updateEndTime
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Error message
        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Save Button
        SaveButton(
            onClick = viewModel::validateAndCreateService,
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}