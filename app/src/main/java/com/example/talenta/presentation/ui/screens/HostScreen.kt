package com.example.talenta.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.talenta.navigation.Routes.BottomNavRoute
import com.example.talenta.navigation.Routes.BottomNavRouteExpert
import com.example.talenta.navigation.Routes.Route


@Composable
fun HostScreen(
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
            else -> null
        }
    }

    if (flag == null) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            bottomBar = {
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


infix fun NavDestination?.isEqualTo(route: Route): Boolean {
    return this?.hasRoute(route::class) == true
}