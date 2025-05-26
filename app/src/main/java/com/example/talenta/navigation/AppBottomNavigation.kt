package com.example.talenta.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.talenta.data.model.Role
import com.example.talenta.navigation.Routes.BottomNavRoute
import com.example.talenta.navigation.Routes.BottomNavRouteExpert
import com.example.talenta.navigation.Routes.BottomNavRouteSponsor
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.ui.screens.HostViewModel
import com.example.talenta.presentation.ui.screens.isEqualTo

@Composable
fun BottomNavBar(modifier: Modifier = Modifier, viewModel: HostViewModel = hiltViewModel(), navController: NavController) {
    val user = viewModel.userFlow.collectAsState()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination

    val shouldShowBottomNav = remember(currentRoute?.route) {
        listOfRoutesToShowBottomNav.any { route ->
            currentRoute?.isEqualTo(route) == true
        }
    }

    if (shouldShowBottomNav)
    {
        when (user.value?.role) {
            Role.EXPERT -> AppBottomNavForExpert(navController = navController)
            Role.ARTIST -> AppBottomNavigationForArtist(navController = navController)
            Role.SPONSOR -> AppBottomNavigationForSponsor(navController = navController)
            else -> {
                // Handle other roles or no role
                AppBottomNavigationForArtist(navController = navController) // Default to Artist for simplicity
            }
        }
    }

}

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


@Composable
fun AppBottomNavigationForSponsor(navController: NavController) {
    val items = listOf(
        BottomNavRouteSponsor.SponsorDashBoard,
        BottomNavRouteSponsor.SponsorApplication,
        BottomNavRouteSponsor.SponsorProfile
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


val listOfRoutesToShowBottomNav = listOf(
    Route.Notice,
    Route.Dashboard,
    Route.MyBookings,
    Route.Experts,
    Route.Profile,
    Route.SponsorProfile,
    Route.SponsorApplication,
    Route.SponsorProfile,
)