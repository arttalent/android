package com.example.talenta.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.talenta.jitsi.jitsi
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.ui.auth.AuthScreen
import com.example.talenta.ui.auth.login.ForgotPasswordScreen
import com.example.talenta.ui.auth.login.LoginScreen
import com.example.talenta.ui.auth.login.PasswordResetSuccessScreen
import com.example.talenta.ui.auth.signup.OTPVerificationScreen
import com.example.talenta.ui.auth.signup.SignUpAs
import com.example.talenta.ui.auth.signup.SignUpScreen
import com.example.talenta.ui.auth.signup.SuccessScreen
import com.example.talenta.ui.home.HomeScreen
import com.example.talenta.ui.onboarding.OnboardingScreen

@Composable
fun AppNavigation(
    isLoggedIn: Boolean,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        //startDestination = if (isLoggedIn) Route.Home.path else Route.Onboarding.path
        startDestination = Route.Home.path
    ) {
        composable(Route.Onboarding.path) {
            OnboardingScreen(
                onComplete = { navController.navigate(Route.Auth.path) }
            )
        }

        composable(Route.Auth.path) {
            AuthScreen(navController)
        }

        composable(Route.Home.path) {
            HomeScreen()
        }

        composable(Route.SignUpAs.path) {
            SignUpAs(navController)
        }

        composable(Route.SignUp.path) {
            SignUpScreen(navController)
        }

        composable(Route.OTPVerification.path) {
            OTPVerificationScreen(navController)
        }

        composable(Route.Success.path) {
            SuccessScreen(navController)
        }

        composable(Route.Login.path) {
            LoginScreen(navController)
        }

        composable(Route.ForgotPassword.path) {
            ForgotPasswordScreen(navController)
        }

        composable(Route.PasswordResetSuccess.path) {
            PasswordResetSuccessScreen(navController)
        }
    }
}