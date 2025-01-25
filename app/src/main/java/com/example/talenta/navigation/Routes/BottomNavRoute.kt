package com.example.talenta.navigation.Routes

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.talenta.R

sealed class BottomNavRoute(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Experts : BottomNavRoute("experts", "Experts", Icons.Default.Person)
    object MyBookings : BottomNavRoute("my_bookings", "My Bookings", Icons.Default.ThumbUp)
    object Notice : BottomNavRoute("notice", "Notice", Icons.Default.Notifications)
    object Profile : BottomNavRoute("profile", "Profile", Icons.Default.Person)
}