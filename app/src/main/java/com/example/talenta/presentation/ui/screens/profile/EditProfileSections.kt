package com.example.talenta.presentation.ui.screens.profile

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.talenta.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController
) {
    var currentStep by remember { mutableStateOf(0) }
    val totalSteps = 4
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val royalBlue = Color(ContextCompat.getColor(context, R.color.royal_blue))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep > 0) {
                            currentStep--
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
                            currentStep--
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
                        if (currentStep < totalSteps - 1) {
                            currentStep++
                        } else {
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = royalBlue)
                ) {
                    Text(
                        text = if (currentStep == totalSteps - 1) "Save" else "Save & Next"
                    )
                }
            }
        }
    ) { paddingValues ->
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
                repeat(totalSteps) { step ->
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
                0 -> BasicInfoSection()
                1 -> PersonalInfoSection()
                2 -> BioAndCertificatesSection()
                3 -> SocialMediaSection()
            }
        }
    }
}

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
private fun BasicInfoSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var state by remember { mutableStateOf("") }

        Field(
            header = "First Name",
            hint = "Ex: Ramesh",
            value = state,
            onValueChange = { state = it }
        )
        Field(
            header = "Last Name",
            hint = "Ex: Rao",
            value = state,
            onValueChange = { state = it }
        )
        Field(
            header = "Email",
            hint = "Ex: Abc@gmail.com",
            value = state,
            onValueChange = { state = it }
        )
        Field(
            header = "Profession",
            hint = "Ex: Singer",
            value = state,
            onValueChange = { state = it }
        )

    }
}

@Composable
private fun PersonalInfoSection() {

    var state by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//
//            Field(
//                header = "Country Code",
//                hint = "+91",
//                value = state,
//                onValueChange = { state = it },
//                modifier = Modifier.weight(0.3f)
//            )
//            Field(
//                header = "Mobile Number",
//                hint = "Enter Number",
//                value = state,
//                onValueChange = { state = it },
//                modifier = Modifier.weight(0.7f)
//            )
//        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Field(
                header = "City",
                hint = "Ex: Hyderabad",
                value = state,
                onValueChange = { state = it },
                modifier = Modifier.weight(0.5f)
            )

            Field(
                header = "Country",
                hint = "Ex: India",
                value = state,
                onValueChange = { state = it },
                modifier = Modifier.weight(0.5f)
            )
        }
        Field(
            header = "Gender",
            hint = "Ex: Male",
            value = state,
            onValueChange = { state = it }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Field(
                header = "Age",
                hint = "Ex: 18",
                value = state,
                onValueChange = { state = it },
                modifier = Modifier.weight(0.5f)
            )
            Field(
                header = "Birth Year",
                hint = "Ex: 2004",
                value = state,
                onValueChange = { state = it },
                modifier = Modifier.weight(0.5f)
            )
        }
        Field(
            header = "Language",
            hint = "Ex: English",
            value = state,
            onValueChange = { state = it }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Field(
                header = "Height",
                hint = "Ex: 5.8",
                value = state,
                onValueChange = { state = it },
                modifier = Modifier.weight(0.5f)
            )

            Field(
                header = "Weight",
                hint = "Ex: 64Kg",
                value = state,
                onValueChange = { state = it },
                modifier = Modifier.weight(0.5f)
            )
        }
    }
}

@Composable
private fun BioAndCertificatesSection() {

    var bioText by remember { mutableStateOf("") }
    var awardTitle by remember { mutableStateOf("") }
    var organizationName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        // Bio Section
        Text(
            text = "Bio",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )
        Field(
            header = "",
            hint = "Ex: English",
            value = bioText,
            onValueChange = { bioText = it }
        )

        Spacer(Modifier.height(10.dp))

        OutlinedCard(
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Awards Section
            Text(
                text = "Awards",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(8.dp)
            )

            Field(
                header = "Award Title",
                hint = "Name",
                value = awardTitle,
                onValueChange = { awardTitle = it },
                modifier = Modifier.padding(4.dp)
            )

            Field(
                header = "Organisation Name",
                hint = "Name",
                value = organizationName,
                onValueChange = { organizationName = it },
                modifier = Modifier.padding(4.dp)
            )

            // Add Button
            Button(
                onClick = { /* Handle add action */ },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF3F4F6)
                )
            ) {
                Text(
                    text = "+ Add",
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun SocialMediaSection() {

    var linkedinUrl by remember { mutableStateOf("") }
    var facebookUrl by remember { mutableStateOf("") }
    var instagramUrl by remember { mutableStateOf("") }

    var bioText by remember { mutableStateOf("") }
    var awardTitle by remember { mutableStateOf("") }
    var organizationName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        OutlinedCard(
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
        ) {
            // Awards Section
            Text(
                text = "Certificates",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(8.dp)
            )

            Field(
                header = "Certificates name",
                hint = "Name",
                value = awardTitle,
                onValueChange = { awardTitle = it },
                modifier = Modifier.padding(4.dp)
            )

            Field(
                header = "Organisation Name",
                hint = "Name",
                value = organizationName,
                onValueChange = { organizationName = it },
                modifier = Modifier.padding(4.dp)
            )

            // Add Button
            Button(
                onClick = { /* Handle add action */ },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF3F4F6)
                )
            ) {
                Text(
                    text = "+ Add",
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }

        Text(
            text = "Certificates",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = linkedinUrl,
            onValueChange = { linkedinUrl = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste Link", color = Color.LightGray) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray,
            ),
            leadingIcon = {
                Image(painter = painterResource(R.drawable.linkedin), "")
            }
        )

        OutlinedTextField(
            value = facebookUrl,
            onValueChange = { facebookUrl = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste Link", color = Color.LightGray) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray,
            ),
            leadingIcon = {
                Image(painter = painterResource(R.drawable.facebook), "")
            }
        )

        OutlinedTextField(
            value = instagramUrl,
            onValueChange = { instagramUrl = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste Link", color = Color.LightGray) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray,
            ),
            leadingIcon = {
                Image(painter = painterResource(R.drawable.instagram), "")
            }
        )

        OutlinedTextField(
            value = instagramUrl,
            onValueChange = { instagramUrl = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste Link", color = Color.LightGray) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray,
            ),
            leadingIcon = {
                Image(painter = painterResource(R.drawable.twitter), "")
            }
        )
    }
}