package com.example.talenta.presentation.ui.screens.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.talenta.R
import com.example.talenta.presentation.state.ProfileUiState
import com.example.talenta.presentation.ui.screens.profile.tabs.DetailsTab
import com.example.talenta.presentation.ui.screens.profile.tabs.MediaContent
import com.example.talenta.presentation.ui.screens.profile.tabs.ReviewsTab
import com.example.talenta.presentation.viewmodels.ArtistProfileViewModel

@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit,
    viewModel: ArtistProfileViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Details", "Media", "Reviews")
    val royalBlue = colorResource(R.color.royal_blue)

    // Collect the profile state
    val profileState by viewModel.profileState.collectAsState()

    // Image picker launcher
    val context = LocalContext.current

    var toastMessage by remember { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadProfilePhoto(it) { e ->
                toastMessage = e?.message
            }
        }
    }


    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            toastMessage = null
        }
    }
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 6.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (profileState) {
                is ProfileUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = royalBlue)
                    }
                }

                is ProfileUiState.Error -> {
                    // Show error state
                    val errorMessage = (profileState as ProfileUiState.Error).message
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Couldn't load profile",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.Red
                            )
                            Text(
                                text = errorMessage,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.fetchArtistProfile() }) {
                                Text("Retry")
                            }
                        }
                    }
                }

                is ProfileUiState.Success -> {
                    val artist = (profileState as ProfileUiState.Success).user

                    // Profile Header with photo
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray), // Placeholder color
                            contentAlignment = Alignment.Center
                        ) {
                            // Load the profile photo
                            if (artist.profilePicture.isNotEmpty()) {
                                AsyncImage(
                                    model = artist.profilePicture,
                                    contentDescription = "${artist.firstName} ${artist.lastName}",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                // Fallback if no photo URL
                                Text(
                                    text = "${artist.firstName.firstOrNull() ?: ""}${artist.lastName.firstOrNull() ?: ""}",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                        }

                        // Edit Icon - clickable to select new photo
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .background(colorResource(R.color.royal_blue), CircleShape)
                                .padding(4.dp)
                                .clickable { photoPickerLauncher.launch("image/*") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile Picture",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Username - use firstName and lastName if available
                    Text(
                        text = if (artist.firstName.isNotEmpty() || artist.lastName.isNotEmpty())
                            "${artist.firstName} ${artist.lastName}".trim() else "User Name",
                        modifier = Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    // Art Form Label - use profession if available
                    Text(
                        text = artist.professionalData.profession.ifEmpty { "Art form" },
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.Gray
                    )

                    // Edit Profile Button
                    OutlinedButton(
                        onClick = onEditProfileClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent
                        ),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text("Edit Profile", color = Color.Gray)
                    }

                    // Tabs
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        containerColor = Color.Transparent,
                        contentColor = royalBlue,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = royalBlue,
                                height = 3.dp
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = {
                                    Text(
                                        text = title,
                                        color = if (selectedTabIndex == index) royalBlue else Color.Gray,
                                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }

                    // Content based on selected tab
                    when (selectedTabIndex) {
                        0 -> DetailsTab(artist)
                        1 -> MediaContent(viewModel)
                        2 -> ReviewsTab()
                    }
                }
            }
        }
    }
}

