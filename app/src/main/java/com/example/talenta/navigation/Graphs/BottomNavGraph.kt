package com.example.talenta.navigation.Graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.talenta.navigation.Routes.BottomNavRoute
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.ui.screens.DashBoardScreen
import com.example.talenta.presentation.ui.screens.experts.ExpertsScreen
import com.example.talenta.presentation.ui.screens.HostScreen
import com.example.talenta.presentation.ui.screens.MyBookingsScreen
import com.example.talenta.presentation.ui.screens.ReportScreen
import com.example.talenta.presentation.ui.screens.profile.ProfileScreen

fun NavGraphBuilder.bottomNavGraph(navController: NavHostController) {
    navigation(
        startDestination =BottomNavRoute.DashBoard.route,
        route = "host"
    ) {
        composable(BottomNavRoute.DashBoard.route) {
            HostScreen(
                navController = navController,
                content = { DashBoardScreen() }
            )
        }
        composable(BottomNavRoute.Experts.route) {
            HostScreen(
                navController = navController,
                content = { ExpertsScreen(
                    navController
                ) }
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
                content = { ReportScreen() }
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
}