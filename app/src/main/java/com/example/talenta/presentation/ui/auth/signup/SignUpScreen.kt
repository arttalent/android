package com.example.talenta.presentation.ui.auth.signup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.ui.screens.profile.Field
import com.example.talenta.presentation.viewmodels.AuthUiStatee
import com.example.talenta.presentation.viewmodels.SignUpViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Form states
    var fname by remember { mutableStateOf("") }
    var lname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("+91") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Validation
    val isValidEmail = remember(email) {
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    val isValidPassword = remember(password) {
        password.length >= 6
    }

    val isValidPhone = remember(phoneNumber) {
        phoneNumber.length >= 10 && phoneNumber.all { it.isDigit() }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiStatee.Success -> {
                Toast.makeText(
                    context,
                    "Account created successfully, please login",
                    Toast.LENGTH_LONG
                ).show()
                navController.navigate(Route.Login.path)
            }

            is AuthUiStatee.Error -> {
                errorMessage = (uiState as AuthUiStatee.Error).message
                showError = true
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Header
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Join our creative community",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Form Card

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                // Name Fields Row

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

                // Terms and Conditions
                Text(
                    "By creating account you agree to our Terms and Conditions, and our Privacy Policy",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // Sign Up Button
                Button(
                    onClick = {
                        when {
                            password != confirmPassword -> {
                                errorMessage = "Passwords do not match"
                                showError = true
                            }

                            !isValidPhone -> {
                                errorMessage = "Please enter valid phone number"
                                showError = true
                            }

                            else -> {
                                viewModel.startSignUp(
                                    firstName = fname,
                                    lastName = lname,
                                    email = email,
                                    password = password,
                                    countryCode = code,
                                    phoneNumber = phoneNumber
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = fname.isNotBlank() && lname.isNotBlank() && isValidEmail &&
                            isValidPassword && password == confirmPassword && isValidPhone
                ) {
                    if (uiState is AuthUiStatee.Loading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            "Create Account",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // Error Snackbar
    if (showError) {
        Snackbar(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            containerColor = MaterialTheme.colorScheme.errorContainer,
            action = {
                TextButton(onClick = { showError = false }) {
                    Text(
                        "Dismiss",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        ) {
            Text(
                errorMessage,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }

}