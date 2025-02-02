package com.example.talenta.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.talenta.navigation.AppBottomNavigation
import com.example.talenta.navigation.Routes.BottomNavRoute


@Composable
fun HostScreen(
    navController: NavController,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            // Only show bottom navigation if we're on a bottom nav screen
            if (navController.currentBackStackEntryAsState().value?.destination?.route in BottomNavRoute.getAllRoutes()) {
                AppBottomNavigation(navController)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            content()
        }
    }
}