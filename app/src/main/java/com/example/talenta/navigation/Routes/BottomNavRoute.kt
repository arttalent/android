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

// Bottom nav for the sponsor
sealed class BottomNavRouteSponsor(
    val route: Route, val title: String, val icon: Int
) {
    object SponsorDashBoard :
        BottomNavRouteSponsor(Route.Dashboard, "Dashboard", R.drawable.dashboard)

    object SponsorArtist :
        BottomNavRouteSponsor(Route.SponsorArtist, "Artist", R.drawable.sponsor_artist)

    object SponsorExpert :
        BottomNavRouteSponsor(Route.SponsorExpert, "Experts", R.drawable.sponsor_expert)

    object SponsorApplication : BottomNavRouteSponsor(
        Route.SponsorApplication, "Sponsorship", R.drawable.sponsor_application_icon
    )

    object SponsorProfile :
        BottomNavRouteSponsor(Route.Profile, "Profile", R.drawable.sponsor_profile)
}


sealed class BottomNavRouteExpert(
    val route: Route, val title: String, val icon: Int
) {

    object ExpertDashboard :
        BottomNavRouteExpert(Route.Dashboard, "ExpertDashBoard", R.drawable.dashboard)

    object ExpertMyBookings :
        BottomNavRouteExpert(Route.MyBookings, "Bookings", R.drawable.mybookings)

    object ExpertProfile : BottomNavRouteExpert(Route.Profile, "Profile", R.drawable.profile)
    companion object {
        fun getAllRoutes(): List<Route> = listOf(
            ExpertDashboard.route, ExpertMyBookings.route, ExpertProfile.route
        )
    }
}