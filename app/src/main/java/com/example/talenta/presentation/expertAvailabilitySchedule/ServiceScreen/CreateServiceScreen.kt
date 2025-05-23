package com.example.talenta.presentation.expertAvailabilitySchedule.ServiceScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.talenta.presentation.expertAvailabilitySchedule.ServiceScreen.CreateServiceComponents.CreateServiceContent
import com.example.talenta.ui.theme.TalentATheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateServiceScreen(
    viewModel: CreateServiceViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onServiceCreated: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Handle service creation success
    LaunchedEffect(uiState.isServiceCreated) {
        if (uiState.isServiceCreated) {
            onServiceCreated()
            viewModel.resetServiceCreated()
        }
    }

    // Show error message
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            // You can show a snackbar or toast here
            // For now, we'll just clear it after showing
            kotlinx.coroutines.delay(3000)
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top App Bar
        CreateServiceTopBar(onNavigateBack = onNavigateBack)

        // Content
        CreateServiceContent(
            uiState = uiState,
            viewModel = viewModel,
            modifier = Modifier.fillMaxSize()
        )

        // Loading overlay
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateServiceTopBar(onNavigateBack: () -> Unit) {
    TopAppBar(
        title = { Text("Create Service", fontWeight = FontWeight.Medium) },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black
        )
    )
}



@Composable
fun SaveButton(
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val primaryBlue = Color(0xFF4A6FFF)

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryBlue,
            disabledContainerColor = Color.Gray
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Save")
    }
}


@Preview
@Composable
private fun CreateServiceScreenPreview() {
    TalentATheme {
        CreateServiceScreen()
    }
}