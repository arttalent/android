package com.example.talenta.presentation.ui.screens.sponsor.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SponsorApplicationScreen() {
    Box(
        modifier = Modifier
            .padding(50.dp)
    ) {
        Text(
            text = "Sponsor Applications",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            fontFamily = FontFamily.SansSerif,
        )
    }
}