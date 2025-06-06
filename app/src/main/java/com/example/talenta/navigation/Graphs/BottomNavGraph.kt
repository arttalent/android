package com.example.talenta.navigation.Graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.talenta.data.model.User
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.navigation.navTypes.UserNavType
import com.example.talenta.presentation.expertAvailabilitySchedule.CreateServiceScreen
import com.example.talenta.presentation.myBookings.MyBookingsScreen
import com.example.talenta.presentation.ui.screens.DashBoard
import com.example.talenta.presentation.ui.screens.ReportScreen
import com.example.talenta.presentation.ui.screens.experts.ExpertDetailedScreen
import com.example.talenta.presentation.ui.screens.experts.ExpertsScreen
import com.example.talenta.presentation.ui.screens.profile.ProfileScreen
import com.example.talenta.presentation.ui.screens.sponsor.components.SponsorApplicationScreen
import com.example.talenta.presentation.ui.screens.sponsor.components.SponsorArtistScreen
import com.example.talenta.presentation.ui.screens.sponsor.components.SponsorExpertScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.appNavGraph(navController: NavHostController) {
    navigation<Route.HostGraph>(
        startDestination = Route.Dashboard
    ) {
        composable<Route.Dashboard> {
            DashBoard()
        }


        // Sponsor Nav

        composable<Route.SponsorApplication> { SponsorApplicationScreen() }
        composable<Route.SponsorExpert> { SponsorExpertScreen(navController) }
        composable<Route.SponsorArtist> { SponsorArtistScreen(navController) }
        composable<Route.SponsorProfile> { }


        composable<Route.Experts> {
            ExpertsScreen(
                navController
            )

        }

        composable<Route.ExpertDetail>(
            typeMap = mapOf(typeOf<User>() to UserNavType)
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Route.ExpertDetail>()
            ExpertDetailedScreen(navController, args.expert)
        }

//        composable<Route.ServiceTab> {
//            HostScreen(navController) { ServiceTab() }
//        }


        composable<Route.MyBookings> {
            MyBookingsScreen()
        }

        composable<Route.Notice> {
            ReportScreen()
        }

        composable<Route.CreateServiceScreen> {
            CreateServiceScreen(
                onNavigateBack = { navController.popBackStack() },

                onServiceCreated = {
                    navController.navigate(Route.Profile) {
                        popUpTo(Route.Dashboard) { inclusive = true }
                    }
                })
        }

        composable<Route.Profile> {
            ProfileScreen(
                onEditProfileClick = { navController.navigate(Route.EditProfile) },
                onLogoutClick = {
                    navController.navigate(Route.AuthGraph) {
                        popUpTo(Route.Dashboard) { inclusive = true }
                    }
                },
                navigateToCreateService = {
                    navController.navigate(Route.CreateServiceScreen)
                }, onEditService = { service ->

                })
        }

    }
}