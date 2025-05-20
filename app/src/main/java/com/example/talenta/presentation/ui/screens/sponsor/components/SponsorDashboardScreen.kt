package com.example.talenta.presentation.ui.screens.sponsor.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.talenta.presentation.ui.screens.sponsor.SponsorViewModel

@Composable
fun SponsorDashboardScreen(
    viewModel: SponsorViewModel = hiltViewModel()
) {
    val users = viewModel.user.value.user

    LazyColumn(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(users) { user ->
            Text(text = user.firstName, fontWeight = FontWeight.Bold)
        }
    }
}
