package com.example.talenta.navigation.Graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.ui.auth.AuthScreen
import com.example.talenta.presentation.ui.auth.login.ForgotPasswordScreen
import com.example.talenta.presentation.ui.auth.login.LoginScreen
import com.example.talenta.presentation.ui.auth.login.OTPVerificationScreen
import com.example.talenta.presentation.ui.auth.login.PasswordResetSuccessScreen
import com.example.talenta.presentation.ui.auth.signup.SignUpAs
import com.example.talenta.presentation.ui.auth.signup.SignUpScreen
import com.example.talenta.presentation.ui.auth.signup.SuccessScreen
import com.example.talenta.presentation.ui.onboarding.OnboardingScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation<Route.AuthGraph>(
        startDestination = Route.Onboarding
    ) {
        composable<Route.Onboarding> {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Route.Auth)
                })
        }
        composable<Route.Auth> {
            AuthScreen(navController)
        }
        composable<Route.SignUpAs> {
            SignUpAs(navController)
        }
        composable<Route.SignUp> { navBackStackEntry ->
            val role = navBackStackEntry.toRoute<Route.SignUp>().role
            SignUpScreen(role, navController)
        }
        composable<Route.OTPVerification> {
            OTPVerificationScreen(navController)
        }
        composable<Route.Success> {
            SuccessScreen(
                onSuccess = {
                    navController.navigate(Route.HostGraph) {
                        popUpTo(Route.AuthGraph) { inclusive = true }
                        launchSingleTop = true
                    }
                })
        }
        composable<Route.Login> {
            LoginScreen(
                navController = navController, onLoginSuccess = {
                    navController.navigate(Route.HostGraph) {
                        popUpTo(Route.AuthGraph) { inclusive = true }
                        launchSingleTop = true
                    }
                })
        }
        composable<Route.ForgotPassword> {
            ForgotPasswordScreen(navController)
        }
        composable<Route.PasswordResetSuccess> {
            PasswordResetSuccessScreen(navController)
        }
    }
}