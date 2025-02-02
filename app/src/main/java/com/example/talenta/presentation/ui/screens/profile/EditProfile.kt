package com.example.talenta.presentation.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.talenta.data.model.Artist
import com.example.talenta.data.model.SocialMediaLinks
import com.example.talenta.presentation.ui.components.ErrorSnackbar
import com.example.talenta.presentation.ui.components.LoadingDialog
import com.example.talenta.presentation.viewmodels.ProfileUiState
import com.example.talenta.presentation.viewmodels.ProfileViewModel


@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val currentArtist by viewModel.artist.collectAsState()
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Form states
    var firstName by remember { mutableStateOf(currentArtist?.firstName ?: "") }
    var lastName by remember { mutableStateOf(currentArtist?.lastName ?: "") }
    var profession by remember { mutableStateOf(currentArtist?.profession ?: "") }
    var subProfession by remember { mutableStateOf(currentArtist?.subProfession ?: "") }
    var countryCode by remember { mutableStateOf(currentArtist?.countryCode ?: "") }
    var mobileNumber by remember { mutableStateOf(currentArtist?.mobileNumber ?: "") }
    var gender by remember { mutableStateOf(currentArtist?.gender ?: "") }
    var age by remember { mutableStateOf(currentArtist?.age?.toString() ?: "") }
    var birthYear by remember { mutableStateOf(currentArtist?.birthYear?.toString() ?: "") }
    var language by remember { mutableStateOf(currentArtist?.language ?: "") }
    var height by remember { mutableStateOf(currentArtist?.height ?: "") }
    var weight by remember { mutableStateOf(currentArtist?.weight ?: "") }
    var city by remember { mutableStateOf(currentArtist?.city ?: "") }
    var country by remember { mutableStateOf(currentArtist?.country ?: "") }
    var bioData by remember { mutableStateOf(currentArtist?.bioData ?: "") }
    var facebook by remember { mutableStateOf(currentArtist?.socialMediaLinks?.facebook ?: "") }
    var instagram by remember { mutableStateOf(currentArtist?.socialMediaLinks?.instagram ?: "") }
    var linkedin by remember { mutableStateOf(currentArtist?.socialMediaLinks?.linkedin ?: "") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is ProfileUiState.Success -> onNavigateBack()
            is ProfileUiState.Error -> {
                errorMessage = (uiState as ProfileUiState.Error).message
                showError = true
            }

            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Profile Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Selected Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                } else {
                    AsyncImage(
                        model = currentArtist?.photoUrl,
                        contentDescription = "Current Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                }

                IconButton(
                    onClick = { imagePicker.launch("image/*") },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Icon(Icons.Default.Edit, "Change Profile Picture")
                }
            }

            // Form Fields
            ProfileInputField(
                label = "First Name",
                value = firstName,
                onValueChange = { firstName = it }
            )

            ProfileInputField(
                label = "Last Name",
                value = lastName,
                onValueChange = { lastName = it }
            )

            ProfileDropdownField(
                label = "Profession",
                value = profession,
                options = listOf("Singer", "Dancer", "Musician", "Director", "Actor"),
                onValueChange = { profession = it }
            )

            ProfileInputField(
                label = "Sub Profession",
                value = subProfession,
                onValueChange = { subProfession = it }
            )

            ProfileInputField(
                label = "Country Code",
                value = countryCode,
                onValueChange = { countryCode = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            ProfileInputField(
                label = "Mobile Number",
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            ProfileDropdownField(
                label = "Gender",
                value = gender,
                options = listOf("Male", "Female", "Other"),
                onValueChange = { gender = it }
            )

            ProfileInputField(
                label = "Age",
                value = age,
                onValueChange = { age = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Add remaining fields...

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val updatedArtist = Artist(
                        id = currentArtist?.id ?: "",
                        firstName = firstName,
                        lastName = lastName,
                        profession = profession,
                        subProfession = subProfession,
                        countryCode = countryCode,
                        mobileNumber = mobileNumber,
                        photoUrl = currentArtist?.photoUrl ?: "",
                        gender = gender,
                        age = age.toIntOrNull() ?: 0,
                        birthYear = birthYear.toIntOrNull() ?: 0,
                        language = language,
                        height = height,
                        weight = weight,
                        city = city,
                        country = country,
                        bioData = bioData,
                        socialMediaLinks = SocialMediaLinks(
                            facebook = facebook,
                            instagram = instagram,
                            linkedin = linkedin
                        )
                    )
                    viewModel.updateProfile(updatedArtist, selectedImageUri)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Save Changes")
            }
        }

        if (uiState is ProfileUiState.Loading) {
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

@Composable
fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        keyboardOptions = keyboardOptions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDropdownField(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
