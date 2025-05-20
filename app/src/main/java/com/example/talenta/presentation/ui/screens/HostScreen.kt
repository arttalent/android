package com.example.talenta.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.talenta.navigation.AppBottomNavForExpert
import com.example.talenta.navigation.AppBottomNavigationForArtist
import com.example.talenta.navigation.AppBottomNavigationForSponsor
import com.example.talenta.navigation.Routes.BottomNavRoute
import com.example.talenta.navigation.Routes.BottomNavRouteExpert
import com.example.talenta.navigation.Routes.BottomNavRouteSponsor
import com.example.talenta.navigation.Routes.Route
import timber.log.Timber


@Composable
fun HostScreen(
    navController: NavController,
    content: @Composable () -> Unit,
) {
    val viewModel: HostViewModel = hiltViewModel()
    val role = viewModel.role.collectAsState()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination

    val currentRoutes = currentDestinationRouteName(navController)

    val flag = remember(role.value) {
        when (role.value) {
            "EXPERT" -> 1
            "ARTIST" -> 2
            "SPONSOR" -> 3
            "FAN" -> 4
            else -> null
        }
    }

    val fabScreensExpert = listOf(
        Route.ExpertDashboard::class.qualifiedName,
        Route.MyBookings::class.qualifiedName,
        Route.Profile::class.qualifiedName
    )


    val showFab = role.value == "EXPERT" && currentRoutes in fabScreensExpert

    if (flag == null) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(floatingActionButton = {
            if (showFab) {
                Fab { Timber.tag("FAB").d("Clicked") }
            }
        }, bottomBar = {
            when (flag) {
                1 -> {

                    if (BottomNavRouteExpert.getAllRoutes().any { currentRoute isEqualTo it }) {
                        AppBottomNavForExpert(navController)
                    }
                }

                2 -> {
                    if (BottomNavRoute.getAllRoutes().any { currentRoute isEqualTo it }) {
                        AppBottomNavigationForArtist(navController)
                    }
                }

                3 -> {
                    if (BottomNavRouteSponsor.getAllRoutes().any { currentRoute isEqualTo it }) {
                        AppBottomNavigationForSponsor(navController)
                    }
                }

            }
        }) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                content()
            }
        }
    }
}

@Composable
fun currentDestinationRouteName(navController: NavController): String? {
    val backStackEntry = navController.currentBackStackEntryAsState().value
    return backStackEntry?.destination?.route
}

@Composable
fun Fab(onClick: () -> Unit) {
    FloatingActionButton(
        containerColor = androidx.compose.ui.graphics.Color.Blue,
        contentColor = androidx.compose.ui.graphics.Color.White,
        shape = CircleShape,
        onClick = { onClick() },
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}

infix fun NavDestination?.isEqualTo(route: Route): Boolean {
    return this?.hasRoute(route::class) == true
}