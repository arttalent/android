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
import com.example.talenta.presentation.ui.screens.ExpertDashBoardScreen
import com.example.talenta.presentation.ui.screens.HostScreen
import com.example.talenta.presentation.ui.screens.ReportScreen
import com.example.talenta.presentation.ui.screens.experts.ExpertDetailedScreen
import com.example.talenta.presentation.ui.screens.experts.ExpertsScreen
import com.example.talenta.presentation.ui.screens.profile.ProfileScreen
import com.example.talenta.presentation.ui.screens.sponsor.components.SponsorDashboardScreen
import kotlin.reflect.typeOf

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


        composable<Route.ExpertDetail>(
            typeMap = mapOf(typeOf<User>() to UserNavType)
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Route.ExpertDetail>()
            HostScreen(
                navController = navController, content = {
                    ExpertDetailedScreen(navController, args.expert)
                }
            )

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
                navController = navController, content = { CreateServiceScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onServiceCreated = { navController.navigate(Route.ExpertDashboard) }
                ) })
        }



        composable<Route.Profile> {
            var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

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