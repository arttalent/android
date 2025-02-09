package com.example.talenta.navigation.Routes

import com.example.talenta.R

sealed class BottomNavRoute(
    val route: String,
    val title: String,
    val icon: Int
) {
    object DashBoard : BottomNavRoute("dashboard", "DashBoard", R.drawable.dashboard)
    object Experts : BottomNavRoute("experts", "Experts", R.drawable.experts)
    object MyBookings : BottomNavRoute("my_bookings", "Bookings", R.drawable.mybookings)
    object Notice : BottomNavRoute("notice", "Report", R.drawable.report)
    object Profile : BottomNavRoute("profile", "Profile", R.drawable.profile)

    companion object {
        fun getAllRoutes(): List<String> = listOf(
            DashBoard.route,
            Experts.route,
            MyBookings.route,
            Notice.route,
            Profile.route
        )
    }
}