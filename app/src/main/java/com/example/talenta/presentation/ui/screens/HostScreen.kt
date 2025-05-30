package com.example.talenta.presentation.ui.screens

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.example.talenta.navigation.Routes.Route


@Composable
fun Fab(modifier: Modifier= Modifier,onClick: () -> Unit) {
    FloatingActionButton(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        shape = CircleShape,
        onClick = { onClick() },
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}

infix fun NavDestination?.isEqualTo(route: Route): Boolean {
    return this?.hasRoute(route::class) == true
}