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
import com.example.talenta.presentation.ui.screens.HostScreen
import com.example.talenta.presentation.ui.screens.booking.MyBookingsScreen
import com.example.talenta.presentation.ui.screens.booking.ReportScreen
import com.example.talenta.presentation.ui.screens.experts.ExpertDashBoardScreen
import com.example.talenta.presentation.ui.screens.experts.ExpertsScreen
import com.example.talenta.presentation.ui.screens.experts.tabs.DashBoardScreen
import com.example.talenta.presentation.ui.screens.profile.ProfileScreen
import com.example.talenta.presentation.ui.screens.sponsor.components.SponsorApplicationScreen
import com.example.talenta.presentation.ui.screens.sponsor.components.SponsorArtistScreen
import com.example.talenta.presentation.ui.screens.sponsor.components.SponsorDashboardScreen
import com.example.talenta.presentation.ui.screens.sponsor.components.SponsorExpertScreen

fun NavGraphBuilder.bottomNavGraph(navController: NavHostController, startDestination: Route) {
    navigation<Route.HostGraph>(
        startDestination = startDestination
    ) {
        composable<Route.Dashboard> {

            HostScreen(
                navController = navController, content = { DashBoardScreen() })
        }

        composable<Route.ExpertDashboard> {
            HostScreen(

                navController = navController, content = { ExpertDashBoardScreen() })
        }

        // Sponsor Nav

        composable<Route.SponsorDashboard> {
            HostScreen(
                navController = navController, content = {
                    SponsorDashboardScreen()
                })
        }

        composable<Route.SponsorApplication> {
            HostScreen(
                navController = navController, content = { SponsorApplicationScreen() })
        }

//        composable<Route.SponsorProfile> {
//            HostScreen(
//                navController = navController, content = { () })
//        }

        composable<Route.SponsorArtist> {
            HostScreen(
                navController = navController, content = { SponsorArtistScreen() })
        }

        composable<Route.SponsorExpert> {
            HostScreen(
                navController = navController, content = { SponsorExpertScreen() })
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