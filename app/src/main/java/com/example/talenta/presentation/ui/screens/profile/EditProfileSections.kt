package com.example.talenta.presentation.ui.screens.profile


import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.talenta.R
import com.example.talenta.data.model.Certificate
import com.example.talenta.presentation.viewmodels.EditProfileEvent
import com.example.talenta.presentation.viewmodels.EditProfileState
import com.example.talenta.presentation.viewmodels.EditProfileViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val currentStep by viewModel.currentStep.collectAsState()
    val event by viewModel.event.collectAsState()

    val context = LocalContext.current
    val royalBlue = Color(ContextCompat.getColor(context, R.color.royal_blue))
    val scrollState = rememberScrollState()

    // Handle one-time events
    LaunchedEffect(event) {
        when (event) {
            is EditProfileEvent.NavigateBack -> {
                navController.navigateUp()
            }

            is EditProfileEvent.ShowSuccessMessage -> {
                // Show success message AND navigate back
                Toast.makeText(
                    context,
                    "Profile updated successfully",
                    Toast.LENGTH_SHORT
                ).show()
                navController.navigateUp()
            }

            is EditProfileEvent.ShowError -> {
                // Show error but don't navigate
                Toast.makeText(
                    context,
                    (event as EditProfileEvent.ShowError).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is EditProfileEvent.CertificateOperationSuccess -> {
                // This is handled in the BioAndCertificatesSection - don't navigate
            }

            null -> {}
        }
        viewModel.onEventHandled()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep > 0) {
                            viewModel.setCurrentStep(currentStep - 1)
                        } else {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentStep > 0) {
                            viewModel.setCurrentStep(currentStep - 1)
                        } else {
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Back")
                }

                Button(
                    onClick = {
                        if (currentStep < 3) {
                            viewModel.setCurrentStep(currentStep + 1)
                        } else {
                            viewModel.saveProfile()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = royalBlue)
                ) {
                    Text(
                        text = if (currentStep == 3) "Save" else "Save & Next"
                    )
                }
            }
        }
    ) { paddingValues ->
        when (state) {
            is EditProfileState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is EditProfileState.Error -> {
                // Show error state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${(state as EditProfileState.Error).message}")
                }
            }

            is EditProfileState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(scrollState)
                ) {
                    // Progress Indicator
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(4) { step ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        if (step <= currentStep) royalBlue else
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Form content based on current step
                    when (currentStep) {
                        0 -> BasicInfoSection(viewModel)
                        1 -> PersonalInfoSection(viewModel)
                        2 -> BioAndCertificatesSection(viewModel)
                        3 -> SocialMediaSection(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun BasicInfoSection(viewModel: EditProfileViewModel) {
    val state = viewModel.state.collectAsState().value
    if (state !is EditProfileState.Success) return

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Field(
            header = "First Name",
            hint = "Ex: Ramesh",
            value = state.firstName,
            onValueChange = { viewModel.updateField("firstName", it) }
        )
        Field(
            header = "Last Name",
            hint = "Ex: Rao",
            value = state.lastName,
            onValueChange = { viewModel.updateField("lastName", it) }
        )
        Field(
            header = "Email",
            hint = "Ex: Abc@gmail.com",
            value = state.email,
            onValueChange = { viewModel.updateField("email", it) }
        )
        Field(
            header = "Profession",
            hint = "Ex: Singer",
            value = state.profession,
            onValueChange = { viewModel.updateField("profession", it) }
        )
    }
}

@Composable
private fun PersonalInfoSection(viewModel: EditProfileViewModel) {
    val state = viewModel.state.collectAsState().value
    if (state !is EditProfileState.Success) return

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Field(
                header = "City",
                hint = "Ex: Hyderabad",
                value = state.city,
                onValueChange = { viewModel.updateField("city", it) },
                modifier = Modifier.weight(0.5f)
            )

            Field(
                header = "Country",
                hint = "Ex: India",
                value = state.country,
                onValueChange = { viewModel.updateField("country", it) },
                modifier = Modifier.weight(0.5f)
            )
        }

        Field(
            header = "Gender",
            hint = "Ex: Male",
            value = state.gender,
            onValueChange = { viewModel.updateField("gender", it) }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Field(
                header = "Age",
                hint = "Ex: 18",
                value = state.age,
                onValueChange = { viewModel.updateField("age", it) },
                modifier = Modifier.weight(0.5f)
            )

            Field(
                header = "Birth Year",
                hint = "Ex: 2004",
                value = state.birthYear,
                onValueChange = { viewModel.updateField("birthYear", it) },
                modifier = Modifier.weight(0.5f)
            )
        }

        Field(
            header = "Language",
            hint = "Ex: English",
            value = state.language,
            onValueChange = { viewModel.updateField("language", it) }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Field(
                header = "Height",
                hint = "Ex: 5.8",
                value = state.height,
                onValueChange = { viewModel.updateField("height", it) },
                modifier = Modifier.weight(0.5f)
            )

            Field(
                header = "Weight",
                hint = "Ex: 64Kg",
                value = state.weight,
                onValueChange = { viewModel.updateField("weight", it) },
                modifier = Modifier.weight(0.5f)
            )
        }
    }
}

@Composable
fun BioAndCertificatesSection(viewModel: EditProfileViewModel) {
    val state = viewModel.state.collectAsState().value
    if (state !is EditProfileState.Success) return

    val context = LocalContext.current;

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Observe events
    val event by viewModel.event.collectAsState()
    LaunchedEffect(event) {
        when (event) {
            is EditProfileEvent.CertificateOperationSuccess -> {
                isLoading = false
                // Show success toast but DON'T navigate
                Toast.makeText(
                    context,
                    "Certificate operation successful",
                    Toast.LENGTH_SHORT
                ).show()
            }

            is EditProfileEvent.ShowSuccessMessage -> {
                isLoading = false
                // This may be handled elsewhere for navigation
            }

            is EditProfileEvent.ShowError -> {
                isLoading = false
                // Show error message
                Toast.makeText(
                    context,
                    (event as EditProfileEvent.ShowError).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> { /* No action needed */
            }
        }
        viewModel.onEventHandled()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(
            text = "Bio",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )
        Field(
            header = "",
            hint = "Tell us about yourself",
            value = state.bioData,
            onValueChange = { viewModel.updateField("bioData", it) }
        )

        Spacer(Modifier.height(10.dp))

        OutlinedCard(
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "Certificates",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                )

                if (state.certificates.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                    ) {

                        items(state.certificates) { certificate ->
                            CertificateItem(
                                certificate = certificate,
                                onDelete = {
                                    isLoading = true
                                    viewModel.deleteCertificate(certificate)
                                }
                            )
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                } else {
                    Text(
                        "No certificates added yet",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }

                Button(
                    onClick = { showAddDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Add Certificate")
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddCertificateDialog(
            selectedImageUri = selectedImageUri,
            onImageSelected = { selectedImageUri = it },
            onDismiss = {
                showAddDialog = false
                selectedImageUri = null
            },
            onAdd = { description ->
                selectedImageUri?.let { uri ->
                    isLoading = true
                    viewModel.addCertificate(uri, description)
                }
                showAddDialog = false
                selectedImageUri = null
            }
        )
    }
}

@Composable
private fun SocialMediaSection(viewModel: EditProfileViewModel) {
    val state = viewModel.state.collectAsState().value
    if (state !is EditProfileState.Success) return

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Social Media Links",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = state.linkedin,
            onValueChange = { viewModel.updateField("linkedin", it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste Link", color = Color.LightGray) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray,
            ),
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.linkedin),
                    contentDescription = "LinkedIn"
                )
            }
        )

        OutlinedTextField(
            value = state.facebook,
            onValueChange = { viewModel.updateField("facebook", it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste Link", color = Color.LightGray) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray,
            ),
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.facebook),
                    contentDescription = "Facebook"
                )
            }
        )

        OutlinedTextField(
            value = state.instagram,
            onValueChange = { viewModel.updateField("instagram", it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste Link", color = Color.LightGray) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray,
            ),
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.instagram),
                    contentDescription = "Instagram"
                )
            }
        )
    }
}

@Composable
private fun CertificateItem(
    certificate: Certificate,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = certificate.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.certificate)
        )

        Spacer(Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = certificate.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Added: ${formatDate(certificate.timestamp)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@Composable
private fun AddCertificateDialog(
    selectedImageUri: Uri?,
    onImageSelected: (Uri) -> Unit,
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    var description by remember { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Add Certificate",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(16.dp))

                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Change Image")
                    }
                } else {
                    Button(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Select Image")
                    }
                }

                Spacer(Modifier.height(16.dp))

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Description") },
                    maxLines = 3
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { onAdd(description) },
                        enabled = selectedImageUri != null && description.isNotBlank()
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

// Helper function to format timestamp
private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return format.format(date)
}

