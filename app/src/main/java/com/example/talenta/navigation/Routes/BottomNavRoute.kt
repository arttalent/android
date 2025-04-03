package com.example.talenta.navigation.Routes

import com.example.talenta.R
import com.example.talenta.navigation.Routes.Route.Dashboard

sealed class BottomNavRoute(
    val route: Route,
    val title: String,
    val icon: Int
) {
    object DashBoard : BottomNavRoute(Dashboard, "DashBoard", R.drawable.dashboard)
    object Experts : BottomNavRoute(Route.Experts, "Experts", R.drawable.experts)
    object MyBookings : BottomNavRoute(Route.MyBookings, "Bookings", R.drawable.mybookings)
    object Notice : BottomNavRoute(Route.Notice, "Report", R.drawable.report)
    object Profile : BottomNavRoute(Route.Profile, "Profile", R.drawable.profile)

    companion object {
        fun getAllRoutes(): List<Route> = listOf(
            DashBoard.route,
            Experts.route,
            MyBookings.route,
            Notice.route,
            Profile.route
        )
    }
}