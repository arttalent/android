package com.example.talenta.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.talenta.data.model.Role

@Composable
fun DashBoard(
    hostViewModel: HostViewModel = hiltViewModel()
) {
    val user = hostViewModel.userFlow.collectAsState()
    val role = user.value?.role

    when (role) {
        Role.EXPERT -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text("Home Screen For Expert", fontWeight = FontWeight.Bold)
            }
        }
        Role.ARTIST -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text("Home Screen For Artist", fontWeight = FontWeight.Bold)
            }
        }
        Role.SPONSOR -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text("Home Screen For Expert", fontWeight = FontWeight.Bold)
            }
        }
        Role.FAN -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text("Home Screen For Expert", fontWeight = FontWeight.Bold)
            }
        }
        null -> {

        }
    }


}