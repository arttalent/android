package com.example.talenta.navigation.Graphs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.expertAvailabilitySchedule.CreateServiceScreen
import com.example.talenta.presentation.ui.screens.ExpertDashBoardScreen
import com.example.talenta.presentation.ui.screens.HostScreen
import com.example.talenta.presentation.myBookings.MyBookingsScreen
import com.example.talenta.presentation.ui.screens.ReportScreen
import com.example.talenta.presentation.ui.screens.experts.ExpertsScreen
import com.example.talenta.presentation.ui.screens.profile.ProfileScreen
import com.example.talenta.presentation.ui.screens.sponsor.components.SponsorDashboardScreen

fun NavGraphBuilder.bottomNavGraph(navController: NavHostController, startDestination: Route) {
    navigation<Route.HostGraph>(
        startDestination = startDestination
    ) {
        composable<Route.Dashboard> {
            HostScreen(
                navController = navController, content = { SponsorDashboardScreen() })
        }

        composable<Route.ExpertDashboard> {
            HostScreen(
                navController = navController, content = { ExpertDashBoardScreen() })
        }

        composable<Route.SponsorDashboard> {
            HostScreen(
                navController = navController, content = { ExpertDashBoardScreen() })
        }

        composable<Route.SponsorApplication> {
            HostScreen(
                navController = navController, content = { ExpertDashBoardScreen() })
        }

        composable<Route.SponsorProfile> {
            HostScreen(
                navController = navController, content = { ExpertDashBoardScreen() })
        }


        composable<Route.Experts> {
            HostScreen(
                navController = navController, content = {
                    ExpertsScreen(
                        navController
                    )
                })
        }

//        composable<Route.ServiceTab> {
//            HostScreen(navController) { ServiceTab() }
//        }


        composable<Route.MyBookings> {
            HostScreen(
                navController = navController, content = { MyBookingsScreen() })
        }

        composable<Route.Notice> {
            HostScreen(
                navController = navController, content = { ReportScreen() })
        }
        composable<Route.CreateServiceScreen> {
            HostScreen(
                navController = navController, content = { CreateServiceScreen() })
        }



        composable<Route.Profile> {
            var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

            HostScreen(
                navController = navController,
                showFab = selectedTabIndex == 3, // Show FAB only on Service tab
                content = {
                    ProfileScreen(
                        onEditProfileClick = { navController.navigate(Route.EditProfile) },
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { selectedTabIndex = it })
                })
        }

    }
}