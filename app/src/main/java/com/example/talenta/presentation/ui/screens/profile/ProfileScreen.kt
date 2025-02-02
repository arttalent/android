package com.example.talenta.presentation.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.talenta.R
import com.example.talenta.data.model.Artist
import com.example.talenta.presentation.ui.components.ErrorSnackbar
import com.example.talenta.presentation.ui.components.LoadingDialog
import com.example.talenta.presentation.viewmodels.ProfileUiState
import com.example.talenta.presentation.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onEditProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val artist by viewModel.artist.collectAsState()
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(contentAlignment = Alignment.BottomEnd) {
                if (artist?.photoUrl?.isNotEmpty() == true) {
                    AsyncImage(
                        model = artist?.photoUrl,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${artist?.firstName} ${artist?.lastName}",
                fontSize = 20.sp
            )
            Text(
                text = artist?.profession ?: "",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onEditProfileClick) {
                Text(text = "Edit Profile")
            }

            // Display other profile information
            ProfileInfoSection(artist)
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
private fun ProfileInfoSection(artist: Artist?) {
    artist?.let {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileInfoItem("Profession", "${it.profession} - ${it.subProfession}")
                ProfileInfoItem("Contact", "${it.countryCode} ${it.mobileNumber}")
                ProfileInfoItem("Gender", it.gender)
                ProfileInfoItem("Age", it.age.toString())
                ProfileInfoItem("Birth Year", it.birthYear.toString())
                ProfileInfoItem("Language", it.language)
                ProfileInfoItem("Location", "${it.city}, ${it.country}")
                ProfileInfoItem("Bio", it.bioData)

                // Social Media Links
                Text(
                    text = "Social Media",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                it.socialMediaLinks.let { links ->
                    if (links.facebook.isNotEmpty()) ProfileInfoItem("Facebook", links.facebook)
                    if (links.instagram.isNotEmpty()) ProfileInfoItem("Instagram", links.instagram)
                    if (links.linkedin.isNotEmpty()) ProfileInfoItem("LinkedIn", links.linkedin)
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

