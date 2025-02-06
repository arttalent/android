package com.example.talenta.presentation.ui.auth.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.talenta.R
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.ui.components.ErrorSnackbar
import com.example.talenta.presentation.ui.components.LoadingDialog
import com.example.talenta.presentation.ui.screens.profile.Field
import com.example.talenta.presentation.viewmodels.AuthUiState
import com.example.talenta.presentation.viewmodels.AuthViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var fname by remember { mutableStateOf("") }
    var lname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.OtpSent -> navController.navigate(Route.OTPVerification.path)
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Field(
                    header = "First Name",
                    hint = "Ex: Ramesh",
                    value = fname,
                    onValueChange = { fname = it }
                )
                Field(
                    header = "Last Name",
                    hint = "Ex: Rao",
                    value = lname,
                    onValueChange = { lname = it }
                )
                Field(
                    header = "Email",
                    hint = "Ex: Abc@gmail.com",
                    value = email,
                    onValueChange = { email = it }
                )
                Field(
                    header = "Password",
                    hint = "Enter Password",
                    value = password,
                    onValueChange = { password = it }
                )
                Field(
                    header = "Confirm Password",
                    hint = "Confirm Password",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Field(
                        header = "Country Code",
                        hint = "+91",
                        value = code,
                        onValueChange = { code = it },
                        modifier = Modifier.weight(0.3f)
                    )
                    Field(
                        header = "Mobile Number",
                        hint = "Enter Number",
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        modifier = Modifier.weight(0.7f)
                    )
                }
            }

            Column {
                TextButton(
                    onClick = { },
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Text(
                        "By creating account means you agree to the Terms and Conditions, and our Privacy Policy",
                        color = Color.Gray
                    )
                }

                Button(
                    onClick = {
                        when {
                            password != confirmPassword -> {
                                errorMessage = "Passwords do not match"
                                showError = true
                            }

                            phoneNumber.isEmpty() -> {
                                errorMessage = "Please enter phone number"
                                showError = true
                            }

                            else -> {
                                navController.navigate(Route.Login.path)
                                //viewModel.startSignUp(name, email, phoneNumber)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.royal_blue),
                        contentColor = colorResource(R.color.white)
                    )
                ) {
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.titleMedium
                    )
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