package com.example.talenta.presentation.ui.screens.profile.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talenta.R
import com.example.talenta.data.model.Artist


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailsTab(artist: Artist) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // About Section
        ProfileCard(
            title = "About ${artist.person.firstName}",
            content = {
                Text(
                    text = artist.person.bioData.ifEmpty { "No bio information available" },
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                // Social Media Links
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    artist.person.socialMediaLinks.facebook.takeIf { it.isNotEmpty() }?.let {
                        SocialMediaIcon(platform = "facebook")
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    artist.person.socialMediaLinks.instagram.takeIf { it.isNotEmpty() }?.let {
                        SocialMediaIcon(platform = "instagram")
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    artist.person.socialMediaLinks.linkedin.takeIf { it.isNotEmpty() }?.let {
                        SocialMediaIcon(platform = "linkedin")
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    artist.person.socialMediaLinks.twitter.takeIf { it.isNotEmpty() }?.let {
                        SocialMediaIcon(platform = "twitter")
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Appearance Section
        ProfileCard(
            title = "Appearance",
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left Column
                    Column {
                        ProfileAttribute(label = "Age", value = artist.person.age.toString())
                        ProfileAttribute(label = "Ethnicity", value = artist.person.ethnicity)
                    }

                    // Right Column
                    Column {
                        ProfileAttribute(label = "Gender", value = artist.person.gender)
                        ProfileAttribute(
                            label = "Height", value = "${artist.person.height}cm"
                        )
                        ProfileAttribute(
                            label = "Weight", value = "${artist.person.weight}kg"
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Skills Section
        ProfileCard(
            title = "Skills",
            content = {
                FlowRow(
                    maxItemsInEachRow = 3,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    artist.person.skills.forEach { skill ->
                        SkillChip(skill = skill)
                    }
                }
            }
        )
    }
}

@Composable
private fun ProfileCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            content()
        }
    }
}

@Composable
private fun ProfileAttribute(
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(
            text = "$label: $value",
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
private fun SocialMediaIcon(platform: String) {
    val iconResId = when (platform.lowercase()) {
        "linkedin" -> R.drawable.linkedin
        "facebook" -> R.drawable.facebook
        "instagram" -> R.drawable.instagram
        "twitter" -> R.drawable.twitter
        else -> return
    }

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .clickable { /* Handle click to open social media link */ },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = platform,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SkillChip(skill: String) {
    Surface(
        modifier = Modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFFFF9C4)
    ) {
        Text(
            text = skill,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}
