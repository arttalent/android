package com.example.talenta.navigation.Routes

import com.example.talenta.R

sealed class BottomNavRoute(
    val route: Route, val title: String, val icon: Int
) {
    object DashBoard : BottomNavRoute(Route.Dashboard, "DashBoard", R.drawable.dashboard)
    object Experts : BottomNavRoute(Route.Experts, "Experts", R.drawable.experts)
    object MyBookings : BottomNavRoute(Route.MyBookings, "Bookings", R.drawable.mybookings)
    object Notice : BottomNavRoute(Route.Notice, "Report", R.drawable.report)
    object Profile : BottomNavRoute(Route.Profile, "Profile", R.drawable.profile)

    companion object {
        fun getAllRoutes(): List<Route> = listOf(
            DashBoard.route, Experts.route, MyBookings.route, Notice.route, Profile.route
        )
    }
}

sealed class BottomNavRouteExpert(
    val route: Route, val title: String, val icon: Int
) {

    object ExpertDashboard :
        BottomNavRouteExpert(Route.ExpertDashboard, "ExpertDashBoard", R.drawable.dashboard)

    object ExpertMyBookings :
        BottomNavRouteExpert(Route.MyBookings, "Bookings", R.drawable.mybookings)

    object ExpertProfile : BottomNavRouteExpert(Route.Profile, "Profile", R.drawable.profile)
    companion object {
        fun getAllRoutes(): List<Route> = listOf(
            ExpertDashboard.route, ExpertMyBookings.route, ExpertProfile.route
        )
    }
}