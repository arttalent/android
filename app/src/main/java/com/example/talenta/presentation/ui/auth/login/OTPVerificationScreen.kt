package com.example.talenta.presentation.ui.auth.login

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.talenta.presentation.viewmodels.SignInViewModel

@Composable
fun OTPVerificationScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {

}
/*
@Composable
fun OTPVerificationScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val uiState by viewModel.uiState.collectAsState()
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var otpValues by remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val scope = rememberCoroutineScope()
    var isResendEnabled by remember { mutableStateOf(true) }
    var remainingTime by remember { mutableStateOf(30) }

    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiStatee.Success -> {
                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                navController.navigate(Route.Success.path) {
                    // Clear the back stack to prevent going back to login screens
                    popUpTo(Route.Login.path) { inclusive = true }
                }
            }
            is AuthUiStatee.Error -> {
                errorMessage = (uiState as AuthUiStatee.Error).message
                showError = true
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    // Request focus for first OTP field when screen is launched
    LaunchedEffect(Unit) {
        focusRequesters.firstOrNull()?.requestFocus()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 50.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Verify Your Code",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Enter the passcode you just received on your mobile phone",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                otpValues.forEachIndexed { index, value ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = { newValue ->
                            if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                val newOtpValues = otpValues.toMutableList()
                                newOtpValues[index] = newValue
                                otpValues = newOtpValues

                                if (newValue.isNotEmpty() && index < 5) {
                                    focusRequesters[index + 1].requestFocus()
                                } else if (newValue.isEmpty() && index > 0) {
                                    // Allow backspace navigation
                                    focusRequesters[index - 1].requestFocus()
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequesters[index])
                            .height(56.dp),
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = if (index < 5) ImeAction.Next else ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                if (index < 5) focusRequesters[index + 1].requestFocus()
                            },
                            onDone = {
                                if (otpValues.all { it.isNotEmpty() }) {
                                    val otp = otpValues.joinToString("")
                                    viewModel.verifyOtp(otp)
                                }
                            }
                        ),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    val otp = otpValues.joinToString("")
                    //viewModel.verifyOtp(otp)
                },
                enabled = otpValues.all { it.isNotEmpty() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.royal_blue)
                )
            ) {
                Text("Verify")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isResendEnabled) {
                    Text(
                        text = "Resend OTP",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            if (activity != null) {
                                scope.launch {
                                    isResendEnabled = false
                                    remainingTime = 30
                                    viewModel.resendOtp(activity)

                                    while (remainingTime > 0) {
                                        delay(1000)
                                        remainingTime--
                                    }
                                    isResendEnabled = true
                                }
                            } else {
                                Toast.makeText(context, "Unable to resend OTP. Please try again.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                } else {
                    Text("Resend OTP in ${remainingTime}s")
                }
            }
        }

        if (uiState is AuthUiStatee.Loading) {
            LoadingDialog()
        }

        if (showError) {
            ErrorSnackbar(
                message = errorMessage,
                onDismiss = { showError = false }
            )
        }
    }
} */