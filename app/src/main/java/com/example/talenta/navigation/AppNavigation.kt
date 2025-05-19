package com.example.talenta.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.talenta.data.model.User
import com.example.talenta.navigation.Graphs.authNavGraph
import com.example.talenta.navigation.Graphs.bottomNavGraph
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.navigation.navTypes.UserNavType
import com.example.talenta.presentation.expertAvailabilitySchedule.ExpertAvailabilitySchedule
import com.example.talenta.presentation.expertBooking.ExpertBooking
import com.example.talenta.presentation.myBookings.MyBookingsScreen
import com.example.talenta.presentation.ui.screens.experts.ExpertDetailedScreen
import com.example.talenta.presentation.ui.screens.profile.EditProfileScreen
import kotlin.reflect.typeOf

@Composable
fun AppNavigation(isLoggedIn: Boolean) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Route.HostGraph else Route.AuthGraph
    ) {

        // Auth graph
        authNavGraph(navController)
        // Bottom Graph
        bottomNavGraph(navController)

        // Screens accessible from anywhere
        composable<Route.EditProfile> {
            EditProfileScreen(
                navController = navController
            )
        }
        composable<Route.MyBookings> {
            MyBookingsScreen()
        }
        composable<Route.ExpertDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.ExpertDetail>()
            ExpertDetailedScreen(navController, args.expertId)
        }

        composable<Route.ExpertAvailabilitySetScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.ExpertAvailabilitySetScreen>()
            ExpertAvailabilitySchedule(expertId = args.expertId)
        }

        composable<Route.ExpertBookingScreen>(
            typeMap = mapOf(typeOf<User>() to UserNavType)
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Route.ExpertBookingScreen>()
            ExpertBooking(expertDetails = args.expert, selectedServiceId = args.selectedServiceId)
        }

    }

}