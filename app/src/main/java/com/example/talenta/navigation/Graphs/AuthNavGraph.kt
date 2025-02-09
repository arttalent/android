package com.example.talenta.navigation.Graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.ui.auth.AuthScreen
import com.example.talenta.presentation.ui.auth.login.ForgotPasswordScreen
import com.example.talenta.presentation.ui.auth.login.LoginScreen
import com.example.talenta.presentation.ui.auth.login.PasswordResetSuccessScreen
import com.example.talenta.presentation.ui.auth.signup.OTPVerificationScreen
import com.example.talenta.presentation.ui.auth.signup.SignUpAs
import com.example.talenta.presentation.ui.auth.signup.SignUpScreen
import com.example.talenta.presentation.ui.auth.signup.SuccessScreen
import com.example.talenta.presentation.ui.onboarding.OnboardingScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        startDestination = Route.Onboarding.path,
        route = "auth_graph"
    ) {
        composable(Route.Onboarding.path) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Route.Auth.path)
                }
            )
        }
        composable(Route.Auth.path) {
            AuthScreen(navController)
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
            SuccessScreen(
                onSuccess = {
                    navController.navigate("host") {
                        popUpTo("auth_graph") { inclusive = true }
                        launchSingleTop = true
                    }
                })
        }
        composable(Route.Login.path) {
            LoginScreen(navController = navController,
                onLoginSuccess = {
                    navController.navigate("host") {
                        popUpTo("auth_graph") { inclusive = true }
                        launchSingleTop = true
                    }
                })
        }
        composable(Route.ForgotPassword.path) {
            ForgotPasswordScreen(navController)
        }
        composable(Route.PasswordResetSuccess.path) {
            PasswordResetSuccessScreen(navController)
        }
    }
}