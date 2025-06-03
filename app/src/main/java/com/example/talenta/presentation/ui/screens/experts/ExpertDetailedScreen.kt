package com.example.talenta.presentation.ui.screens.experts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.talenta.R
import com.example.talenta.data.model.User
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.ui.screens.experts.tabs.MediaTabForExpert
import com.example.talenta.presentation.ui.screens.experts.tabs.ProfileContent
import com.example.talenta.presentation.ui.screens.experts.tabs.ServicesContent
import com.example.talenta.presentation.viewmodels.ExpertViewModel

@Composable
fun ExpertDetailedScreen(
    navController: NavController, expert: User
) {


    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        TopBar(navController)
        ProfileSection(expertId = expert.id)
        SocialMediaSection()
        RatingSection(expert.id)

        TabSection(
            selectedTab = selectedTab, onTabSelected = { selectedTab = it })

        // Content based on selected tab
        when (selectedTab) {
            0 -> ProfileContent(expert.id)
            1 -> MediaTabForExpert()
            2 -> ServicesContent(expert) { serviceId ->
                navController.navigate(
                    Route.ExpertBookingScreen(
                        expert = expert, selectedServiceId = serviceId
                    )
                )
            }
        }
    }
}

@Composable
private fun TopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.clickable {
                navController.popBackStack()
            })
    }
}

@Composable
fun ProfileSection(
    expertId: String?, viewModel: ExpertViewModel = hiltViewModel()
) {
    val expert by viewModel.expert.collectAsStateWithLifecycle()



    if (expertId.isNullOrEmpty()) {
        Text("Error: No Expert ID provided")
        return
    }

    LaunchedEffect(expertId) {
        viewModel.getExpertById(expertId)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture


        AsyncImage(
            model = expert?.profilePicture,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.placeholder)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Expert Name
        Text(
            text = (expert?.firstName + expert?.lastName),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // Profession & Location
        Text(text = expert?.professionalData?.profession?.let { "$it | ${expert?.bio?.country}" }
            ?: "", fontSize = 14.sp, color = Color.Gray)

        // Languages
        Text(
            text = expert?.bio?.language ?: "", fontSize = 14.sp, color = Color.Gray
        )
    }
}


@Composable
private fun SocialMediaSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SocialMediaIcon(icon = R.drawable.linkedin)
        Spacer(modifier = Modifier.width(16.dp))
        SocialMediaIcon(icon = R.drawable.facebook)
        Spacer(modifier = Modifier.width(16.dp))
        SocialMediaIcon(icon = R.drawable.instagram)
    }
}

@Composable
private fun SocialMediaIcon(icon: Int) {
    Image(
        painter = painterResource(icon),
        contentDescription = null,
        modifier = Modifier
            .size(29.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { /* Handle social media click */ })
}

@Composable
private fun RatingSection(expertId: String?, viewModel: ExpertViewModel = hiltViewModel()) {


    if (expertId.isNullOrEmpty()) {
        Text("Error: No Expert ID provided")
        return
    }

    LaunchedEffect(expertId) {
        viewModel.getExpertById(expertId)
    }

    // Todo Rating needs to be added
    // println("rating: ${expert?}")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Todo - Fake Rating
        RatingItem(
            rating = "10", description = "50 reviews", stars = true
        )
        RatingItem(
            rating = "110", description = "Followers"
        )
        RatingItem(
            rating = "100+", description = "Assessments evaluated"
        )
    }
}

@Composable
private fun RatingItem(
    rating: String, description: String, stars: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (stars) {
            Row {
                repeat(5) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        } else {
            Text(
                text = rating,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorResource(R.color.royal_blue)
            )
        }
        Text(
            text = description, fontSize = 12.sp, color = Color.Gray
        )
    }
}

@Composable
private fun TabSection(
    selectedTab: Int, onTabSelected: (Int) -> Unit
) {
    val tabs = listOf("Details", "Media", "Services")
    val royalBlue = colorResource(R.color.royal_blue)

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            contentColor = royalBlue,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = royalBlue
                )
            }) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index, onClick = { onTabSelected(index) }, text = {
                    Text(
                        text = title,
                        color = if (selectedTab == index) royalBlue else Color.Gray
                    )
                })
            }
        }
    }
}