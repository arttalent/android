package com.example.talenta.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.talenta.navigation.Graphs.authNavGraph
import com.example.talenta.navigation.Graphs.bottomNavGraph
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.expertAvailabilitySchedule.ExpertAvailabilitySchedule
import com.example.talenta.presentation.expertBooking.ExpertBooking
import com.example.talenta.presentation.ui.screens.HostViewModel
import com.example.talenta.presentation.ui.screens.MyBookingsScreen
import com.example.talenta.presentation.ui.screens.experts.ExpertDetailedScreen
import com.example.talenta.presentation.ui.screens.profile.EditProfileScreen

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun AppNavigation(isLoggedIn: Boolean) {
    val navController = rememberNavController()
    val hostViewModel: HostViewModel = hiltViewModel()
    val roleState = hostViewModel.role.collectAsState()

    val role = roleState.value

    if (isLoggedIn && role == null) {
        LoadingScreen()
        return
    }

    val dynamicStartDestination = when (role) {
        "ARTIST" -> Route.Dashboard
        "EXPERT" -> Route.ExpertDashboard
        else -> Route.Dashboard // fallback
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Route.HostGraph else Route.AuthGraph
    ) {
        authNavGraph(navController)
        bottomNavGraph(navController, dynamicStartDestination)

        // Other screens
        composable<Route.EditProfile> {
            EditProfileScreen(navController = navController)
        }
        composable<Route.MyBookings> {
            MyBookingsScreen()
        }
        composable<Route.ExpertDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.ExpertDetail>()
            ExpertDetailedScreen(navController, args.expertId)
        }
        composable("expert_booking/{expertId}/{serviceId}") { backStackEntry ->
            val expertId = backStackEntry.arguments?.getString("expertId") ?: ""
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
            ExpertBooking(expertId = expertId, serviceId = serviceId)
        }
        composable<Route.ExpertAvailabilitySetScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.ExpertAvailabilitySetScreen>()
            ExpertAvailabilitySchedule(expertId = args.expertId)
        }
        composable<Route.ExpertBookingScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.ExpertBookingScreen>()
            ExpertBooking(expertId = args.expertId, serviceId = args.serviceId)
        }
    }
}
