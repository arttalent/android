package com.example.talenta.presentation.ui.screens.profile.tabs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talenta.R
import com.example.talenta.data.model.SocialMediaLinks
import com.example.talenta.data.model.User


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailsTab(user: User) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Hero About Section
            ProfileCard(
                title = "About ${user.firstName}",
                icon = "👤",
                content = {
                    AboutSection(user = user)
                }
            )

            // Appearance Section with better visual design
            ProfileCard(
                title = "Appearance",
                icon = "✨",
                content = {
                    AppearanceSection(user = user)
                }
            )

            // Skills Section with interactive chips
            ProfileCard(
                title = "Skills & Expertise",
                icon = "🎯",
                content = {
                    SkillsSection(user = user)
                }
            )
        }
    }
}

@Composable
private fun AboutSection(user: User) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Bio with better empty state
        if (user.bio.bioData.isNotEmpty()) {
            Text(
                text = user.bio.bioData,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = Color(0xFF495057),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            EmptyStateBox(
                icon = "📝",
                message = "${user.firstName} hasn't added a bio yet",
                subMessage = "Check back later for more details"
            )
        }

        // Social Media with enhanced design
        SocialMediaSection(user.bio.socialMediaLinks)
    }
}

@Composable
private fun AppearanceSection(user: User) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(200.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            AttributeCard(
                icon = "🎂",
                label = "Age",
                value = if (user.physicalAttributes.age > 0)
                    "${user.physicalAttributes.age} years" else "Not specified"
            )
        }
        item {
            AttributeCard(
                icon = "⚧",
                label = "Gender",
                value = user.physicalAttributes.gender.ifEmpty { "Not specified" }
            )
        }
        item {
            AttributeCard(
                icon = "🌍",
                label = "Ethnicity",
                value = user.physicalAttributes.ethnicity?.name ?: "Not specified"
            )
        }
        item {
            AttributeCard(
                icon = "📏",
                label = "Height",
                value = if (user.physicalAttributes.height > 0.toString())
                    "${user.physicalAttributes.height}cm" else "Not specified"
            )
        }
        item {
            AttributeCard(
                icon = "⚖️",
                label = "Weight",
                value = if (user.physicalAttributes.weight > 0.toString())
                    "${user.physicalAttributes.weight}kg" else "Not specified"
            )
        }
    }
}

@Composable
private fun SkillsSection(user: User) {
    if (user.professionalData.skills.isNotEmpty()) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            user.professionalData.skills.forEach { skill ->
                EnhancedSkillChip(skill = skill)
            }
        }
    } else {
        EmptyStateBox(
            icon = "🛠️",
            message = "No skills listed yet",
            subMessage = "Professional skills will appear here"
        )
    }
}

@Composable
private fun ProfileCard(
    title: String,
    icon: String,
    content: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(true) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 0f else -90f,
        animationSpec = tween(300)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(300, easing = EaseInOutCubic)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        ),
        shape = RoundedCornerShape(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with expand/collapse
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = icon,
                        fontSize = 24.sp
                    )
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212529)
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    modifier = Modifier
                        .rotate(rotationAngle)
                        .size(24.dp),
                    tint = Color(0xFF6C757D)
                )
            }

            // Content with animation
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                content()
            }
        }
    }
}

@Composable
private fun AttributeCard(
    icon: String,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = icon,
                    fontSize = 16.sp
                )
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6C757D)
                )
            }

            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (value.contains("Not specified"))
                    Color(0xFF999999) else Color(0xFF212529),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SocialMediaSection(socialMediaLinks: SocialMediaLinks) {
    val availableLinks = listOf(
        "facebook" to socialMediaLinks.facebook,
        "instagram" to socialMediaLinks.instagram,
        "linkedin" to socialMediaLinks.linkedin,
        "twitter" to socialMediaLinks.twitter
    ).filter { it.second.isNotEmpty() }

    if (availableLinks.isNotEmpty()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Connect on social media",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6C757D)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                availableLinks.forEach { (platform, _) ->
                    EnhancedSocialMediaIcon(platform = platform)
                }
            }
        }
    } else {
        EmptyStateBox(
            icon = "🔗",
            message = "No social media links",
            subMessage = "Connect to see social profiles"
        )
    }
}

@Composable
private fun EnhancedSocialMediaIcon(platform: String) {
    val backgroundColor = when (platform.lowercase()) {
        "linkedin" -> Color(0xFF0077B5)
        "facebook" -> Color(0xFF1877F2)
        "instagram" -> Color(0xFFE4405F)
        "twitter" -> Color(0xFF1DA1F2)
        else -> Color(0xFF6C757D)
    }

    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100)
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                // Handle social media link click
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        val iconResId = when (platform.lowercase()) {
            "linkedin" -> R.drawable.linkedin
            "facebook" -> R.drawable.facebook
            "instagram" -> R.drawable.instagram
            "twitter" -> R.drawable.twitter
            else -> null
        }

        iconResId?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = platform,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
private fun EnhancedSkillChip(skill: String) {
    var isSelected by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF007BFF) else Color(0xFFF8F9FA),
        animationSpec = tween(200)
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFF495057),
        animationSpec = tween(200)
    )

    Surface(
        modifier = Modifier
            .clickable { isSelected = !isSelected }
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        shadowElevation = if (isSelected) 4.dp else 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = skill,
                color = textColor,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun EmptyStateBox(
    icon: String,
    message: String,
    subMessage: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = icon,
                fontSize = 32.sp
            )
            Text(
                text = message,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6C757D),
                textAlign = TextAlign.Center
            )
            Text(
                text = subMessage,
                fontSize = 14.sp,
                color = Color(0xFF999999),
                textAlign = TextAlign.Center
            )
        }
    }
}
