package com.example.talenta.presentation.ui.auth.signup


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.talenta.data.model.Role
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.viewmodels.AuthUiActions
import com.example.talenta.presentation.viewmodels.SignUpEvents
import com.example.talenta.presentation.viewmodels.SignUpViewModel

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
        when (val event = uiEvents.value) {
            is SignUpEvents.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
            SignUpEvents.None -> {}
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
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.05f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Enhanced Header
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Join our creative community and unlock your potential",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Enhanced Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Personal Information Section
                    SectionHeader("Personal Information")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Field(
                            header = "First Name",
                            hint = "John",
                            value = uiState.firstName,
                            onValueChange = {
                                viewModel.handleAction(AuthUiActions.UpdateData(firstName = it))
                            },
                            modifier = Modifier.weight(1f)
                        )
                        Field(
                            header = "Last Name",
                            hint = "Doe",
                            value = uiState.lastName,
                            onValueChange = {
                                viewModel.handleAction(AuthUiActions.UpdateData(lastName = it))
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Contact Information Section
                    SectionHeader("Contact Information")

                    Field(
                        header = "Email Address",
                        hint = "john.doe@example.com",
                        value = uiState.email,
                        onValueChange = {
                            viewModel.handleAction(AuthUiActions.UpdateData(email = it))
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                            hint = "9876543210",
                            value = uiState.phoneNumber,
                            onValueChange = {
                                viewModel.handleAction(AuthUiActions.UpdateData(phoneNumber = it))
                            },
                            modifier = Modifier.weight(0.7f)
                        )
                    }

                    // Professional Information Section
                    SectionHeader("Professional Information")

                    ProfessionDropdown(
                        selectedProfession = uiState.profession ?: "",
                        onProfessionSelected = { profession ->
                            viewModel.handleAction(AuthUiActions.UpdateData(profession = profession))
                        }
                    )

                    // Security Section
                    SectionHeader("Security")

                    Field(
                        header = "Password",
                        hint = "Enter a strong password",
                        value = uiState.password,
                        isPassword = true,
                        onValueChange = {
                            viewModel.handleAction(AuthUiActions.UpdateData(password = it.trim()))
                        }
                    )

                    Field(
                        header = "Confirm Password",
                        hint = "Re-enter your password",
                        value = uiState.confirmPassword,
                        isPassword = true,
                        onValueChange = {
                            viewModel.handleAction(AuthUiActions.UpdateData(confirmPassword = it.trim()))
                        }
                    )

                    // Terms and Conditions
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "By creating an account, you agree to our Terms of Service and Privacy Policy. We're committed to protecting your personal information.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    // Enhanced Sign Up Button
                    Button(
                        onClick = {
                            viewModel.handleAction(AuthUiActions.SignUp)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                "Create Account",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun Field(
    header: String,
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = header,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = hint,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            singleLine = true
        )
    }
}

@Composable
private fun ProfessionDropdown(
    selectedProfession: String,
    onProfessionSelected: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Profession",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true }
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                ),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedProfession.isEmpty()) "Select your profession" else selectedProfession,
                    color = if (selectedProfession.isEmpty())
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )

                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        if (showDialog) {
            ProfessionSelectionDialog(
                selectedProfession = selectedProfession,
                onProfessionSelected = { profession ->
                    onProfessionSelected(profession)
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
private fun ProfessionSelectionDialog(
    selectedProfession: String,
    onProfessionSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val professions = listOf(
        "Musician" to "🎵",
        "Singer" to "🎤",
        "Music Producer" to "🎛️",
        "Music Teacher" to "👨‍🏫",
        "Sound Engineer" to "🔊",
        "Composer" to "🎼",
        "Music Therapist" to "🎶",
        "DJ" to "🎧",
        "Music Director" to "🎭",
        "Instrumentalist" to "🎸",
        "Vocalist" to "🎵",
        "Music Arranger" to "📝",
        "Audio Engineer" to "🎚️",
        "Music Journalist" to "📰",
        "Concert Performer" to "🎪",
        "Studio Musician" to "🏠",
        "Music Conductor" to "🎼",
        "Lyricist" to "✍️",
        "Music Critic" to "📋",
        "Band Leader" to "👑",
        "Session Musician" to "🎹",
        "Music Technician" to "🔧",
        "Music Promoter" to "📢",
        "Record Label Owner" to "💿",
        "Other" to "🎨"
    )

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Dialog Header
                Text(
                    text = "Choose Your Profession",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Select the profession that best describes your role in the music industry",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Professions List
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    professions.forEach { (profession, emoji) ->
                        ProfessionItem(
                            profession = profession,
                            emoji = emoji,
                            isSelected = profession == selectedProfession,
                            onClick = { onProfessionSelected(profession) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Cancel Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Cancel",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfessionItem(
    profession: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = profession,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isSelected) {
                Card(
                    shape = RoundedCornerShape(50),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "✓",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}
