package com.example.talenta.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.talenta.navigation.Routes.BottomNavRoute
import com.example.talenta.navigation.Routes.BottomNavRouteExpert
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.ui.screens.isEqualTo

@Composable
fun AppBottomNavForExpert(navController: NavController) {
    val items = listOf(
        BottomNavRouteExpert.ExpertDashboard,
        BottomNavRouteExpert.ExpertMyBookings,
        BottomNavRouteExpert.ExpertProfile
    )


    NavigationBar {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            val isSelected =
                currentDestination?.hierarchy?.any { it isEqualTo screen.route } == true

            NavigationBarItem(
                icon = { Icon(painterResource(screen.icon), screen.title) },
                label = { Text(screen.title) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            popUpTo(Route.HostGraph) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                })
        }
    }
}

@Composable
fun AppBottomNavigationForArtist(navController: NavController) {
    val items = listOf(
        BottomNavRoute.DashBoard,
        BottomNavRoute.Experts,
        BottomNavRoute.MyBookings,
        BottomNavRoute.Notice,
        BottomNavRoute.Profile
    )

    NavigationBar {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            val isSelected =
                currentDestination?.hierarchy?.any { it isEqualTo screen.route } == true

            NavigationBarItem(
                icon = { Icon(painterResource(screen.icon), screen.title) },
                label = { Text(screen.title) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            popUpTo(Route.HostGraph) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                })
        }
    }
}
