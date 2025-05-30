package com.example.talenta.presentation.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.talenta.data.model.MediaType
import com.example.talenta.presentation.ui.screens.Fab
import com.example.talenta.presentation.viewmodels.ArtistProfileViewModel
import com.example.talenta.presentation.viewmodels.UploadMediaState

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
    modifier: Modifier = Modifier,
    viewModel: ArtistProfileViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    // Add button in the top-right corner
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, end = 16.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Fab {
            showDialog = true
        }
    }


}

@Composable
fun AddMediaDialog(
    onDismiss: () -> Unit,
    viewModel: ArtistProfileViewModel
) {
    var selectedMediaType by remember { mutableStateOf<MediaType?>(null) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var description by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }

    // Observe the upload state
    val uploadState by viewModel.profileState.collectAsState()

    // Set up image/video picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = PickVisualMedia()
    ) { uri: Uri? ->
        selectedUri = uri
    }

    // Effect to handle upload state changes
    LaunchedEffect(uploadState) {
        when (uploadState.uploadMediaState) {
            is UploadMediaState.Success -> {
                isUploading = false
                onDismiss()
            }

            is UploadMediaState.Error -> {
                isUploading = false
                // Show error (could use a snackbar or toast)
            }

            is UploadMediaState.Loading -> {
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
                            selectedMediaType = MediaType.IMAGE
                            photoPickerLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Add Photo")
                    }

                    Button(
                        onClick = {
                            selectedMediaType = MediaType.VIDEO
                            photoPickerLauncher.launch(PickVisualMediaRequest(PickVisualMedia.VideoOnly))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Add Video")
                    }
                } else if (selectedUri != null) {
                    // Media selected, show preview and description field
                    if (selectedMediaType == MediaType.IMAGE) {
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
                                    viewModel.startUpload(
                                        imageUri = uri,
                                        description = description,
                                        mediaType = selectedMediaType ?: MediaType.IMAGE
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
                            if (selectedMediaType == MediaType.IMAGE) {
                                photoPickerLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                            } else {
                                photoPickerLauncher.launch(PickVisualMediaRequest(PickVisualMedia.VideoOnly))
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