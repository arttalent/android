package com.example.talenta.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.talenta.navigation.AppBottomNavigation
import com.example.talenta.navigation.Routes.BottomNavRoute

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {


        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                AppBottomNavigation(navController = navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomNavRoute.Profile.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomNavRoute.Experts.route) {
                    ExpertsScreen()
                }
                composable(BottomNavRoute.MyBookings.route) {
                    MyBookingsScreen()
                }
                composable(BottomNavRoute.Notice.route) {
                    NoticeScreen()
                }
                composable(BottomNavRoute.Profile.route) {
                    ProfileScreen()
                }
            }
        }
    }