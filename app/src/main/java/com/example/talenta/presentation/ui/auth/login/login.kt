package com.example.talenta.presentation.ui.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arpitkatiyarprojects.countrypicker.CountryPickerOutlinedTextField
import com.arpitkatiyarprojects.countrypicker.models.BorderThickness
import com.arpitkatiyarprojects.countrypicker.models.CountriesListDialogDisplayProperties
import com.arpitkatiyarprojects.countrypicker.models.CountryDetails
import com.arpitkatiyarprojects.countrypicker.models.SelectedCountryDisplayProperties
import com.arpitkatiyarprojects.countrypicker.utils.CountryPickerUtils
import com.example.talenta.R
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
    var enteredMobileNumber by remember { mutableStateOf("") }
    var isMobileNumberValidationError by remember { mutableStateOf(false) }
    var formatExampleMobileNumber by remember {
        mutableStateOf(false)
    }
    var selectedCountryDisplayProperties by remember {
        mutableStateOf(SelectedCountryDisplayProperties())
    }

    var countriesListDialogDisplayProperties by remember {
        mutableStateOf(CountriesListDialogDisplayProperties())
    }

    var borderThickness by remember {
        mutableStateOf(BorderThickness())
    }

    var selectedCountryState by remember {
        mutableStateOf<CountryDetails?>(null)
    }

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
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 32.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CountryPickerOutlinedTextField(

                    isError = isMobileNumberValidationError,
                    supportingText = if (isMobileNumberValidationError || enteredMobileNumber.isNotBlank()) {
                        {
                            Text(text = if (isMobileNumberValidationError) "Invalid mobile number" else "Valid mobile number")
                        }
                    } else null,
                    modifier = Modifier.weight(1f),
                    mobileNumber = CountryPickerUtils.getFormattedMobileNumber(
                        enteredMobileNumber, selectedCountryState?.countryCode ?: "IN",
                    ),
                    onMobileNumberChange = {
                        enteredMobileNumber = it
                        isMobileNumberValidationError = !CountryPickerUtils.isMobileNumberValid(
                            enteredMobileNumber,
                            selectedCountryState?.countryCode ?: "IN"
                        )
                    },
                    selectedCountryDisplayProperties = selectedCountryDisplayProperties,
                    countriesListDialogDisplayProperties = countriesListDialogDisplayProperties,
                    borderThickness = borderThickness,
                    onCountrySelected = {
                        selectedCountryState = it
                    },
                    placeholder = {
                        Text(
                            text = "Ex. ${
                                CountryPickerUtils.getExampleMobileNumber(
                                    selectedCountryState?.countryCode ?: "IN",
                                    formatExampleMobileNumber
                                )
                            }"
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedSupportingTextColor = Color(0xFF2eb82e),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray,
                        errorPlaceholderColor = Color.Gray,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    )
                )

            }


            Button(
                onClick = {
                    navController.navigate(Route.OTPVerification.path)
                    //viewModel.sendOtp(number)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.royal_blue)
                ),
                //enabled = isValidPhoneNumber
            ) {

                val formattedNumber = selectedCountryState?.countryCode?.let {
                    CountryPickerUtils.getFormattedMobileNumber(
                        enteredMobileNumber, it
                    )
                }
                println(formattedNumber)

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