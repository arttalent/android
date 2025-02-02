package com.example.talenta.presentation.ui.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.ui.components.ErrorSnackbar
import com.example.talenta.presentation.ui.components.LoadingDialog
import com.example.talenta.presentation.viewmodels.AuthUiState
import com.example.talenta.presentation.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: () -> Unit = {}, // Added this parameter
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }

    // Validate phone number format
    val isValidPhoneNumber = remember(number) {
        number.length >= 10
                //&& number.all { it.isDigit() }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.OtpSent -> {
                navController.navigate(Route.OTPVerification.path)
            }
            is AuthUiState.Success -> {
                onLoginSuccess()
            }
            is AuthUiState.Error -> {
                errorMessage = (uiState as AuthUiState.Error).message
                showError = true
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login to Art Talent",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 32.dp)
            )

            OutlinedTextField(
                value = number,
                onValueChange = {
                    // Only allow digits and limit length
//                    if (it.length <= 15 && (it.isEmpty() || it.all { char -> char.isDigit() })) {
                        number = it

                },
                label = { Text("Enter Mobile Number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                isError = number.isNotEmpty() && !isValidPhoneNumber,
                supportingText = {
                    if (number.isNotEmpty() && !isValidPhoneNumber) {
                        Text("Please enter a valid phone number")
                    }
                }
            )

            Button(
                onClick = { viewModel.sendOtp(number) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4169E1)
                ),
                enabled = isValidPhoneNumber
            ) {
                Text(
                    "Login",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { navController.navigate(Route.ForgotPassword.path) }
                ) {
                    Text("Forgot Password?")
                }
                TextButton(
                    onClick = { navController.navigate(Route.SignUpAs.path) }
                ) {
                    Text("Sign Up")
                }
            }
        }

        if (uiState is AuthUiState.Loading) {
            LoadingDialog()
        }

        if (showError) {
            ErrorSnackbar(
                message = errorMessage,
                onDismiss = { showError = false }
            )
        }
    }
}