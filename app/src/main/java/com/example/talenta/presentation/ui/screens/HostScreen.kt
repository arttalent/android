package com.example.talenta.presentation.ui.screens

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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


@Composable
fun HostScreen(
    showFab: Boolean = false,
    navController: NavController,
    content: @Composable () -> Unit,
) {
    val viewModel: HostViewModel = hiltViewModel()
    val role = viewModel.role.collectAsState()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination


    val flag = remember(role.value) {
        when (role.value) {
            "EXPERT" -> 1
            "ARTIST" -> 2
            "SPONSOR" -> 3
            "FAN" -> 4
            else -> null
        }
    }


    LaunchedEffect(role.value) {
        Log.d("HostScreen", "Current role: ${role.value}")
    }


    if (flag == null) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(floatingActionButton = {
            if (showFab) {
                Fab {
                    navController.navigate(Route.CreateServiceScreen)
                }
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
        containerColor = Color.Blue,
        contentColor = Color.White,
        shape = CircleShape,
        onClick = { onClick() },
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}

infix fun NavDestination?.isEqualTo(route: Route): Boolean {
    return this?.hasRoute(route::class) == true
}