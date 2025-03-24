package com.example.talenta.presentation.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.talenta.R
import com.example.talenta.presentation.viewmodels.ArtistProfileViewModel
import com.example.talenta.presentation.viewmodels.UploadState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    val uploadState by viewModel.uploadState.collectAsState()

    val currentScreen by remember(selectedUri, selectedMediaType) {
        derivedStateOf {
            when {
                selectedUri != null -> "preview"
                selectedMediaType != null -> "retry"
                else -> "selection"
            }
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Move processing off the main thread
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                selectedUri = uri
            }
        }
    }

    LaunchedEffect(uploadState) {
        when (uploadState) {
            is UploadState.Success -> {
                isUploading = false
                onDismiss()
            }

            is UploadState.Error -> {
                isUploading = false
            }

            is UploadState.Loading -> {
                isUploading = true
            }

            else -> { /* Idle state */
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
            color = Color.White,
            modifier = Modifier.widthIn(max = 360.dp)
        ) {
            // Use key to force recomposition only when screen changes
            key(currentScreen) {
                when (currentScreen) {
                    "preview" -> {
                        MediaPreviewScreen(
                            selectedUri = selectedUri,
                            selectedMediaType = selectedMediaType,
                            description = description,
                            onDescriptionChange = { description = it },
                            isUploading = isUploading,
                            onCancel = {
                                selectedMediaType = null
                                selectedUri = null
                                description = ""
                            },
                            onUpload = {
                                selectedUri?.let { uri ->
                                    viewModel.uploadMedia(
                                        imageUri = uri,
                                        description = description,
                                        isVideo = selectedMediaType == "video"
                                    )
                                }
                            }
                        )
                    }

                    "retry" -> {
                        NoMediaSelectedScreen(
                            selectedMediaType = selectedMediaType,
                            onSelectAgain = {
                                if (selectedMediaType == "photo") {
                                    photoPickerLauncher.launch("image/*")
                                } else {
                                    photoPickerLauncher.launch("video/*")
                                }
                            },
                            onChangeMediaType = {
                                selectedMediaType = null
                            }
                        )
                    }

                    else -> {
                        MediaTypeSelectionScreen(
                            onPhotoSelected = {
                                selectedMediaType = "photo"
                                photoPickerLauncher.launch("image/*")
                            },
                            onVideoSelected = {
                                selectedMediaType = "video"
                                photoPickerLauncher.launch("video/*")
                            }
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun MediaTypeSelectionScreen(
    onPhotoSelected: () -> Unit,
    onVideoSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(), // Changed back to fillMaxWidth
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add New Media",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Photo Card with cached resources
            OptimizedMediaCard(
                title = "Upload Photos",
                iconRes = R.drawable.photo,
                onClick = onPhotoSelected,
                modifier = Modifier.weight(1f) // Keep weight for equal width
            )

            // Video Card with cached resources
            OptimizedMediaCard(
                title = "Upload Videos",
                iconRes = R.drawable.video,
                onClick = onVideoSelected,
                modifier = Modifier.weight(1f) // Keep weight for equal width
            )
        }
    }
}

@Composable
private fun OptimizedMediaCard(
    title: String,
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .height(180.dp) // Fixed height instead of aspectRatio and fillMaxHeight
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F8FF)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Use Icon instead of AsyncImage for static resources
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(72.dp),
                tint = colorResource(id = R.color.royal_blue)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}
@Composable
private fun MediaPreviewScreen(
    selectedUri: Uri?,
    selectedMediaType: String?,
    description: String,
    onDescriptionChange: (String) -> Unit,
    isUploading: Boolean,
    onCancel: () -> Unit,
    onUpload: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Use a BoxWithConstraints to maintain stable layout
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val screenHeight = maxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Content that stays visible always
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (selectedMediaType == "photo") "Upload Photo" else "Upload Video",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Fixed height for preview to prevent resizing
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)  // Fixed height that doesn't change
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF5F7FA))
                        .border(1.dp, Color(0xFFE0E4EC), RoundedCornerShape(12.dp))
                ) {
                    if (selectedMediaType == "photo") {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(selectedUri)
                                .crossfade(true)
                                .size(Size.ORIGINAL)
                                .build(),
                            contentDescription = "Selected photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = colorResource(id = R.color.royal_blue),
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            },
                            error = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.video),
                                        contentDescription = "Error loading image",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.video),
                                    contentDescription = "Video selected",
                                    modifier = Modifier.size(40.dp),
                                    tint = colorResource(id = R.color.royal_blue)
                                )
                                Text(
                                    text = "Video selected",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Fixed-height text field that doesn't resize while typing
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF5F7FA))
                        .border(1.dp, Color(0xFFE0E4EC), RoundedCornerShape(12.dp))
                ) {
                    BasicTextField(
                        value = description,
                        onValueChange = onDescriptionChange,
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier.fillMaxWidth()) {
                                if (description.isEmpty()) {
                                    Text(
                                        text = "Describe your media...",
                                        color = Color.Gray,
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f, fill = true))
            }

            // Action buttons always at the bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onCancel()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE0E4EC)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Gray
                    )
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onUpload()
                    },
                    enabled = !isUploading,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.royal_blue),
                        disabledContainerColor = colorResource(id = R.color.royal_blue).copy(alpha = 0.6f)
                    )
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Upload")
                    }
                }
            }
        }
    }
}
@Composable
private fun NoMediaSelectedScreen(
    selectedMediaType: String?,
    onSelectAgain: () -> Unit,
    onChangeMediaType: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No Media Selected",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Icon(
            painter = painterResource(R.drawable.video),
            contentDescription = "No media",
            modifier = Modifier
                .size(72.dp)
                .padding(vertical = 16.dp),
            tint = Color.Gray
        )

        Text(
            text = "Please select ${if (selectedMediaType == "photo") "an image" else "a video"} to upload",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = onSelectAgain,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.royal_blue)
            )
        ) {
            Text("Select Again")
        }

        OutlinedButton(
            onClick = onChangeMediaType,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFFE0E4EC))
        ) {
            Text("Change Media Type")
        }
    }
}