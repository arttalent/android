package com.example.talenta.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.talenta.presentation.expertAvailabilitySchedule.CreateServiceScreen

@Composable
fun ReportScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        CreateServiceScreen()
    }
}