package com.example.talenta.navigation.Routes

import com.example.talenta.R

sealed class BottomNavRoute(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Experts : BottomNavRoute("experts", "Experts", R.drawable.expert)
    object MyBookings : BottomNavRoute("my_bookings", "My Bookings", R.drawable.save)
    object Notice : BottomNavRoute("notice", "Notice", R.drawable.notice)
    object Profile : BottomNavRoute("profile", "Profile", R.drawable.profile)

    companion object {
        fun getAllRoutes(): List<String> = listOf(
            Experts.route,
            MyBookings.route,
            Notice.route,
            Profile.route
        )
    }
}