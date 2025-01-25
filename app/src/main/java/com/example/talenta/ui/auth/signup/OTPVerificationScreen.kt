package com.example.talenta.ui.auth.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.talenta.navigation.Routes.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OTPVerificationScreen(navController: NavController) {
    var otpValues by remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val scope = rememberCoroutineScope()
    var isResendEnabled by remember { mutableStateOf(true) }
    var remainingTime by remember { mutableStateOf(30) }

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
            text = "Enter the passcode you just received on your email address ending with ********in@gmail.com",
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
                TextField(
                    value = value,
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                            val newOtpValues = otpValues.toMutableList()
                            newOtpValues[index] = newValue
                            otpValues = newOtpValues

                            if (newValue.isNotEmpty() && index < 5) {
                                focusRequesters[index + 1].requestFocus()
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { navController.navigate(Route.Success.path) },
            enabled = otpValues.all { it.isNotEmpty() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
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
                        scope.launch {
                            isResendEnabled = false
                            remainingTime = 30
                            while (remainingTime > 0) {
                                delay(1000)
                                remainingTime--
                            }
                            isResendEnabled = true
                        }
                    }
                )
            } else {
                Text("Resend OTP in ${remainingTime}s")
            }
        }
    }
}
