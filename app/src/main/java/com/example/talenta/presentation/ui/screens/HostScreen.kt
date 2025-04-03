package com.example.talenta.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.talenta.navigation.AppBottomNavigation
import com.example.talenta.navigation.Routes.BottomNavRoute
import com.example.talenta.navigation.Routes.Route


@Composable
fun HostScreen(
    navController: NavController,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            // Only show bottom navigation if we're on a bottom nav screen
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination
            if (BottomNavRoute.getAllRoutes().any {
                    currentRoute isEqualTo it
                }) {
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

infix fun NavDestination?.isEqualTo(route: Route): Boolean {
    return this?.hasRoute(route::class) == true
}