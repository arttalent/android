package com.example.talenta.navigation.Graphs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.talenta.data.model.User
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.navigation.navTypes.UserNavType
import com.example.talenta.presentation.expertAvailabilitySchedule.ServiceScreen.CreateServiceScreen
import com.example.talenta.presentation.myBookings.MyBookingsScreen
import com.example.talenta.presentation.ui.screens.DashBoard
import com.example.talenta.presentation.ui.screens.ReportScreen
import com.example.talenta.presentation.ui.screens.experts.ExpertDetailedScreen
import com.example.talenta.presentation.ui.screens.experts.ExpertsScreen
import com.example.talenta.presentation.ui.screens.profile.ProfileScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.appNavGraph(navController: NavHostController) {
    navigation<Route.HostGraph>(
        startDestination = Route.Dashboard
    ) {
        composable<Route.Dashboard> {
            DashBoard()
        }

        composable<Route.SponsorApplication> {
            DashBoard()
        }

        composable<Route.SponsorProfile> {
            DashBoard()
        }


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
                onServiceCreated = { navController.navigate(Route.Dashboard) }
            )
        }



        composable<Route.Profile> {
            var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
            ProfileScreen(
                onEditProfileClick = { navController.navigate(Route.EditProfile) },
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it },
                onLogoutClick = {
                    navController.navigate(Route.AuthGraph) {
                        popUpTo(Route.Dashboard) { inclusive = true }
                    }
                },
                navigateToCreateService = {
                    navController.navigate(Route.CreateServiceScreen)
                }
            )
        }


    }
}