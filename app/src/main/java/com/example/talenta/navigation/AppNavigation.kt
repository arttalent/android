package com.example.talenta.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.talenta.navigation.Graphs.authNavGraph
import com.example.talenta.navigation.Graphs.bottomNavGraph
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.ui.screens.experts.ExpertDetailedScreen
import com.example.talenta.presentation.ui.screens.profile.EditProfileScreen

@Composable
fun AppNavigation(isLoggedIn: Boolean) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (!isLoggedIn) Route.AuthGraph else Route.HostGraph
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
        composable<Route.ExpertDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.ExpertDetail>()
            ExpertDetailedScreen(navController, args.expertId)
        }
    }

}