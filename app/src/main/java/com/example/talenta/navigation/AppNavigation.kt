package com.example.talenta.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.talenta.navigation.Routes.BottomNavRoute
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.ui.screens.ExpertsScreen
import com.example.talenta.presentation.ui.screens.HostScreen
import com.example.talenta.presentation.ui.screens.MyBookingsScreen
import com.example.talenta.presentation.ui.screens.NoticeScreen
import com.example.talenta.presentation.ui.screens.profile.EditProfileScreen
import com.example.talenta.presentation.ui.screens.profile.ProfileScreen

@Composable
fun AppNavigation(isLoggedIn: Boolean) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "host" else "auth_graph"
    ) {

        // Auth graph
        authNavGraph(navController)

        // Host screen with bottom navigation
        navigation(
            startDestination = BottomNavRoute.Experts.route,
            route = "host"
        ) {
            composable(BottomNavRoute.Experts.route) {
                HostScreen(
                    navController = navController,
                    content = { ExpertsScreen() }
                )
            }
            composable(BottomNavRoute.MyBookings.route) {
                HostScreen(
                    navController = navController,
                    content = { MyBookingsScreen() }
                )
            }
            composable(BottomNavRoute.Notice.route) {
                HostScreen(
                    navController = navController,
                    content = { NoticeScreen() }
                )
            }
            composable(BottomNavRoute.Profile.route) {
                HostScreen(
                    navController = navController,
                    content = {
                        ProfileScreen(
                            onEditProfileClick = {
                                navController.navigate(Route.EditProfile.path)
                            }
                        )
                    }
                )
            }
        }

        // Screens accessible from anywhere
        composable(Route.EditProfile.path) {
            EditProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }

}