package com.example.talenta.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.talenta.data.model.Role
import com.example.talenta.navigation.Routes.BottomNavRoute
import com.example.talenta.navigation.Routes.BottomNavRouteExpert
import com.example.talenta.navigation.Routes.BottomNavRouteSponsor
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.ui.screens.HostViewModel
import com.example.talenta.presentation.ui.screens.isEqualTo

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    viewModel: HostViewModel = hiltViewModel(),
    navController: NavController
) {
    val user = viewModel.userFlow.collectAsState()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination

    val shouldShowBottomNav = remember(currentRoute?.route) {
        listOfRoutesToShowBottomNav.any { route ->
            currentRoute?.isEqualTo(route) == true
        }
    }

    if (shouldShowBottomNav) {
        when (user.value?.role) {
            Role.EXPERT -> AppBottomNavForExpert(navController = navController)
            Role.ARTIST -> AppBottomNavigationForArtist(navController = navController)
            Role.SPONSOR -> AppBottomNavigationForSponsor(navController = navController)
            else -> {
                // Handle other roles or no role
                AppBottomNavigationForArtist(navController = navController) // Default to Artist for simplicity
            }
        }
    }

}

@Composable
fun AppBottomNavForExpert(navController: NavController) {
    val items = listOf(
        BottomNavRouteExpert.ExpertDashboard,
        BottomNavRouteExpert.ExpertMyBookings,
        BottomNavRouteExpert.ExpertProfile
    )


    NavigationBar {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            val isSelected =
                currentDestination?.hierarchy?.any { it isEqualTo screen.route } == true

            NavigationBarItem(
                icon = { Icon(painterResource(screen.icon), screen.title) },
                label = { Text(screen.title) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            popUpTo(Route.HostGraph) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                })
        }
    }
}

@Composable
fun AppBottomNavigationForArtist(navController: NavController) {
    val items = listOf(
        BottomNavRoute.DashBoard,
        BottomNavRoute.Experts,
        BottomNavRoute.MyBookings,
        BottomNavRoute.Notice,
        BottomNavRoute.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val selectedIndex = items.indexOfFirst { screen ->
        currentDestination?.hierarchy?.any { it isEqualTo screen.route } == true
    }.takeIf { it >= 0 } ?: 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp) // Reduced height
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF667EEA),
                        Color(0xFF667EEA)

                    )
                ),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, screen ->
                val isSelected = index == selectedIndex

                BottomNavChip(
                    screen = screen,
                    isSelected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            navController.navigate(screen.route) {
                                popUpTo(Route.HostGraph) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomNavChip(
    screen: BottomNavRoute,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
        animationSpec = tween(200),
        label = "color"
    )

    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color.White.copy(alpha = 0.2f) else Color.Transparent,
        animationSpec = tween(200),
        label = "backgroundColor"
    )

    Box(
        modifier = Modifier
            .width(64.dp)
            .height(64.dp)
            .background(
                color = animatedBackgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(screen.icon),
                contentDescription = screen.title,
                tint = animatedColor,
                modifier = Modifier.size(20.dp)
            )

            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn(tween(200)) + slideInVertically { it / 2 },
                exit = fadeOut(tween(150)) + slideOutVertically { it / 2 }
            ) {
                Text(
                    text = screen.title,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun AppBottomNavigationForSponsor(navController: NavController) {
    val items = listOf(
        BottomNavRouteSponsor.SponsorDashBoard,
        BottomNavRouteSponsor.SponsorArtist,
        BottomNavRouteSponsor.SponsorExpert,
        BottomNavRouteSponsor.SponsorApplication,
        BottomNavRouteSponsor.SponsorProfile
    )

    NavigationBar {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            val isSelected =
                currentDestination?.hierarchy?.any { it isEqualTo screen.route } == true

            NavigationBarItem(
                icon = { Icon(painterResource(screen.icon), screen.title) },
                label = { Text(screen.title) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            popUpTo(Route.HostGraph) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                })
        }
    }
}


val listOfRoutesToShowBottomNav = listOf(
    Route.Notice,
    Route.Dashboard,
    Route.MyBookings,
    Route.Experts,
    Route.Profile,
    //Sponsor
    Route.SponsorDashboard,
    Route.SponsorApplication,
    Route.SponsorProfile,
    Route.SponsorExpert,
    Route.SponsorArtist
)