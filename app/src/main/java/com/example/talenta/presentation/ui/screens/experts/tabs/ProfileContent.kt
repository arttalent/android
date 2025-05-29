package com.example.talenta.presentation.ui.screens.experts.tabs

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.talenta.R
import com.example.talenta.data.model.User
import com.example.talenta.presentation.viewmodels.ExpertViewModel

@Composable
fun ProfileContent(expertId: String?, viewModel: ExpertViewModel = hiltViewModel()) {
    LaunchedEffect(expertId) {
        if (expertId != null) {
            viewModel.getExpertById(expertId)
        }
    }

    val expert by viewModel.expert.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8F9FA),
                        Color(0xFFE9ECEF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // About Section
            ProfileCard(
                title = "About ${expert?.firstName ?: "Expert"}",
                icon = "👤",
                content = {
                    AboutSection(expert = expert)
                }
            )

            // Professional Information Section
            ProfileCard(
                title = "Professional Info",
                icon = "🎵",
                content = {
                    ProfessionalSection(expert = expert)
                }
            )

            // Awards & Certificates Section
            ProfileCard(
                title = "Awards & Certificates",
                icon = "🏆",
                content = {
                    AwardsSection(expert = expert)
                }
            )
        }
    }
}

@Composable
private fun AboutSection(expert: User?) {
    if (expert?.bio?.bioData.isNullOrBlank()) {
        EmptyStateBox(
            icon = "📝",
            message = "No biography available",
            subMessage = "${expert?.firstName ?: "Expert"} hasn't added their story yet"
        )
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF8F9FA)
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                if (expert != null) {
                    Text(
                        text = expert.bio.bioData,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = Color(0xFF495057),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Add location if available
                if (expert != null) {
                    if (expert.bio.city.isNotBlank() || expert.bio.country.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📍",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = buildString {
                                    if (expert != null) {
                                        if (expert.bio.city.isNotBlank()) append(expert.bio.city)

                                        if (expert.bio.city.isNotBlank() && expert.bio.country.isNotBlank()) append(
                                            ", "
                                        )
                                        if (expert.bio.country.isNotBlank()) append(expert.bio.country)
                                    }
                                },
                                fontSize = 14.sp,
                                color = Color(0xFF6C757D),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // Add language if available
                if (expert != null) {
                    if (expert.bio.language.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🌐",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            if (expert != null) {
                                Text(
                                    text = expert.bio.language,
                                    fontSize = 14.sp,
                                    color = Color(0xFF6C757D),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfessionalSection(expert: User?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profession Section
        if (expert?.professionalData?.profession?.isNotBlank() == true) {
            EnhancedInfoSection(
                icon = "💼",
                title = "Profession",
                content = buildString {
                    append(expert.professionalData.profession)
                    if (expert.professionalData.subProfession.isNotBlank()) {
                        append(" - ${expert.professionalData.subProfession}")
                    }
                },
                backgroundColor = Color(0xFFE3F2FD)
            )
        }

        // Skills Section
        val skillsContent = if (expert?.professionalData?.skills?.isEmpty() != false) {
            "No skills added"
        } else {
            expert.professionalData.skills.joinToString(", ")
        }

        EnhancedInfoSection(
            icon = "⭐",
            title = "Skills",
            content = skillsContent,
            backgroundColor = Color(0xFFFFF3E0),
            isEmptyState = skillsContent == "No skills added"
        )

        // Certifications Section (if using string list)
        if (expert?.professionalData?.certifications?.isNotEmpty() == true) {
            EnhancedInfoSection(
                icon = "📜",
                title = "Certifications",
                content = expert.professionalData.certifications.joinToString(", "),
                backgroundColor = Color(0xFFE8F5E8)
            )
        }
    }
}

@Composable
private fun AwardsSection(expert: User?) {
    val certificates = expert?.professionalData?.certificatesList

    if (certificates.isNullOrEmpty()) {
        EnhancedAwardItem(
            title = "No Certificates Available",
            subtitle = "Expert has not uploaded any certificates yet",
            imageUrl = null,
            isEmpty = true
        )
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            certificates.forEach { certificate ->
                EnhancedAwardItem(
                    title = certificate.description.ifBlank { "Certificate" },
                    subtitle = certificate.description.ifBlank { "No description available" },
                    imageUrl = certificate.imageUrl.ifBlank { null },
                    isEmpty = false
                )
            }
        }
    }
}
@Composable
private fun EnhancedInfoSection(
    icon: String,
    title: String,
    content: String,
    backgroundColor: Color,
    isEmptyState: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = icon,
                fontSize = 20.sp
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF212529)
                )
                Text(
                    text = content,
                    fontSize = 14.sp,
                    color = if (isEmptyState) Color(0xFF999999) else Color(0xFF495057),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun EnhancedAwardItem(title: String, subtitle: String, imageUrl: String?, isEmpty: Boolean = false) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (!isEmpty) isExpanded = !isExpanded }
            .animateContentSize(
                animationSpec = tween(300, easing = EaseInOutCubic)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isEmpty) Color(0xFFF8F9FA) else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isEmpty) 1.dp else 4.dp,
            pressedElevation = if (isEmpty) 1.dp else 8.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        if (isEmpty) {
            // Empty state design
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "🏆",
                        fontSize = 32.sp
                    )
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF6C757D),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = Color(0xFF999999),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Regular certificate design
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Certificate Image
                    Card(
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.certificate),
                            error = painterResource(id = R.drawable.caution),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Certificate Info
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF212529),
                            maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = subtitle,
                            fontSize = 14.sp,
                            color = Color(0xFF6C757D),
                            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Expand/Collapse Icon
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = Color(0xFF6C757D),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Expanded content (if needed and different)
                if (isExpanded && title != subtitle) {
                    Divider(
                        color = Color(0xFFE9ECEF),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Text(
                        text = "Certificate Details",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF495057),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
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