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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.talenta.data.model.Expert
import com.example.talenta.data.model.Person
import com.example.talenta.data.model.Role
import com.example.talenta.data.model.SignUpData
import com.example.talenta.data.model.SocialMediaLinks
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.state.AuthUiStatee
import com.example.talenta.presentation.ui.screens.profile.Field
import com.example.talenta.presentation.viewmodels.AuthUiActions
import com.example.talenta.presentation.viewmodels.SignUpEvents
import com.example.talenta.presentation.viewmodels.SignUpViewModel
import java.util.UUID

@Composable
fun SignUpScreen(
    role: Role,
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvents = viewModel.events.collectAsStateWithLifecycle(
        initialValue = SignUpEvents.None
    )
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.handleAction(
            AuthUiActions.UpdateData(
                selectedRole = role
            )
        )
    }
    LaunchedEffect(uiEvents.value) {
        val event = uiEvents.value
        when (event) {
            is SignUpEvents.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }

            SignUpEvents.None -> {

            }
            SignUpEvents.SignUpSuccess -> {
                Toast.makeText(
                    context,
                    "Account created successfully, please login",
                    Toast.LENGTH_LONG
                ).show()
                navController.navigate(Route.Login)
            }
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


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {

                Field(
                    header = "First Name",
                    hint = "Ex: Ramesh",
                    value = uiState.firstName,
                    onValueChange = {
                        viewModel.handleAction(AuthUiActions.UpdateData(firstName = it))
                    }
                )
                Field(
                    header = "Last Name",
                    hint = "Ex: Rao",
                    value = uiState.lastName,
                    onValueChange = {
                        viewModel.handleAction(AuthUiActions.UpdateData(lastName = it))
                    }
                )
                Field(
                    header = "Email",
                    hint = "Ex: Abc@gmail.com",
                    value = uiState.email,
                    onValueChange = {
                        viewModel.handleAction(AuthUiActions.UpdateData(email = it))
                    }
                )
                Field(
                    header = "Password",
                    hint = "Enter Password",
                    value = uiState.password,
                    onValueChange = {
                        viewModel.handleAction(AuthUiActions.UpdateData(password = it))
                    }
                )
                Field(
                    header = "Confirm Password",
                    hint = "Confirm Password",
                    value = uiState.confirmPassword,
                    onValueChange = {
                        viewModel.handleAction(AuthUiActions.UpdateData(confirmPassword = it))
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Field(
                        header = "Country Code",
                        hint = "+91",
                        value = uiState.countryCode,
                        onValueChange = {
                            viewModel.handleAction(AuthUiActions.UpdateData(countryCode = it))
                        },
                        modifier = Modifier.weight(0.3f)
                    )
                    Field(
                        header = "Mobile Number",
                        hint = "Enter Number",
                        value = uiState.phoneNumber,
                        onValueChange = {
                            viewModel.handleAction(AuthUiActions.UpdateData(phoneNumber = it))
                        },
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
                        viewModel.handleAction(AuthUiActions.SignUp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    if (uiState.isLoading) {
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
}