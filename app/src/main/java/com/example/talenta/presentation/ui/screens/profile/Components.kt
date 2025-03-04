package com.example.talenta.presentation.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.talenta.R
import com.example.talenta.presentation.viewmodels.ArtistProfileViewModel
import com.example.talenta.presentation.viewmodels.UploadState

@Composable
fun Field(
    header: String,
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(text = header, fontWeight = FontWeight.Bold, maxLines = 1)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(hint, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray,
            )
        )
    }
}


@Composable
fun AddMediaButton(
    viewModel: ArtistProfileViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    // Add button in the top-right corner
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, end = 16.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        IconButton(
            onClick = { showDialog = true }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Media",
                tint = colorResource(R.color.royal_blue)
            )
        }
    }

    if (showDialog) {
        AddMediaDialog(
            onDismiss = { showDialog = false },
            viewModel = viewModel
        )
    }
}

@Composable
fun AddMediaDialog(
    onDismiss: () -> Unit,
    viewModel: ArtistProfileViewModel
) {
    var selectedMediaType by remember { mutableStateOf<String?>(null) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var description by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }

    // Observe the upload state
    val uploadState by viewModel.uploadState.collectAsState()

    // Set up image/video picker
    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedUri = uri
    }

    // Effect to handle upload state changes
    LaunchedEffect(uploadState) {
        when (uploadState) {
            is UploadState.Success -> {
                isUploading = false
                onDismiss()
            }

            is UploadState.Error -> {
                isUploading = false
                // Show error (could use a snackbar or toast)
            }

            is UploadState.Loading -> {
                isUploading = true
            }

            else -> { /* Idle state, no action needed */
            }
        }
    }

    Dialog(
        onDismissRequest = {
            if (!isUploading) onDismiss()
        }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add New Media",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (selectedMediaType == null) {
                    // Media type selection
                    Button(
                        onClick = {
                            selectedMediaType = "photo"
                            photoPickerLauncher.launch("image/*")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Add Photo")
                    }

                    Button(
                        onClick = {
                            selectedMediaType = "video"
                            photoPickerLauncher.launch("video/*")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Add Video")
                    }
                } else if (selectedUri != null) {
                    // Media selected, show preview and description field
                    if (selectedMediaType == "photo") {
                        AsyncImage(
                            model = selectedUri,
                            contentDescription = "Selected photo",
                            modifier = Modifier
                                .size(200.dp)
                                .padding(vertical = 8.dp)
                        )
                    } else {
                        // For video, we'd ideally show a thumbnail or preview
                        // For simplicity, just show a placeholder or text
                        Text(
                            "Video selected",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                selectedMediaType = null
                                selectedUri = null
                                description = ""
                            }
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                selectedUri?.let { uri ->
                                    viewModel.uploadMedia(
                                        imageUri = uri,
                                        description = description,
                                        isVideo = selectedMediaType == "video"
                                    )
                                }
                            },
                            enabled = !isUploading
                        ) {
                            if (isUploading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White
                                )
                            } else {
                                Text("Upload")
                            }
                        }
                    }
                } else {
                    // Uri is null but media type is selected - show error or retry button
                    Text(
                        "No media selected",
                        color = Color.Red,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Button(
                        onClick = {
                            if (selectedMediaType == "photo") {
                                photoPickerLauncher.launch("image/*")
                            } else {
                                photoPickerLauncher.launch("video/*")
                            }
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text("Select Again")
                    }

                    OutlinedButton(
                        onClick = {
                            selectedMediaType = null
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text("Change Media Type")
                    }
                }
            }
        }
    }
}