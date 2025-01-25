package com.example.talenta.ui.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.talenta.ui.onboarding.OnboardingScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthFlow(navController: NavController) {

    var showAuth by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = showAuth,
        transitionSpec = {
            fadeIn() + slideInHorizontally() with
                    fadeOut() + slideOutHorizontally()
        }
    ) { isAuth ->
        if (isAuth) {
            AuthScreen(navController)
        } else {
            OnboardingScreen { showAuth = true }
        }
    }
}