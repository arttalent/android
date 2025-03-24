package com.example.talenta.presentation.ui.screens.profile.tabs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.talenta.R
import com.example.talenta.data.model.Photo
import com.example.talenta.data.model.Video
import com.example.talenta.presentation.ui.screens.profile.AddMediaButton
import com.example.talenta.presentation.viewmodels.ArtistProfileViewModel
import com.example.talenta.presentation.viewmodels.UploadState

@Composable
fun MediaContent(
    viewModel: ArtistProfileViewModel,
    onNavigateToAllMedia: () -> Unit = {}
) {
    val photos by viewModel.photos
    val videos by viewModel.videos
    val uploadState by viewModel.uploadState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Add media button
        AddMediaButton(viewModel = viewModel)

        // Only "All" filter
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(listOf("All")) { filter ->
                Chip(
                    onClick = { /* Only one filter, so no action needed */ },
                    label = {
                        Text(text = filter)
                    },
                    selected = true
                )
            }
        }

        // All Media Section
        val allMedia = (photos + videos).sortedByDescending {
            when (it) {
                is Photo -> it.timestamp
                is Video -> it.timestamp
                else -> 0L
            }
        }

        if (allMedia.isNotEmpty()) {
            Text(
                text = "Media",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allMedia.take(4)) { mediaItem ->
                    when (mediaItem) {
                        is Photo -> PhotoItem(photo = mediaItem)
                        is Video -> VideoItem(video = mediaItem)
                    }
                }
            }

            // See all button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToAllMedia)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "See all",
                    color = colorResource(R.color.royal_blue),
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "See more",
                    tint = colorResource(R.color.royal_blue)
                )
            }
        }

        // Show upload status
        when (uploadState) {
            is UploadState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                        color = colorResource(R.color.royal_blue)
                    )
                }
            }

            is UploadState.Error -> {
                Text(
                    text = "Upload failed: ${(uploadState as UploadState.Error).message}",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> {} // No indication needed for Success or Idle states
        }
    }
}

@Composable
private fun PhotoItem(photo: Photo) {
    OutlinedCard(
        modifier = Modifier
            .size(width = 180.dp, height = 160.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = photo.imageUrl,
                contentDescription = photo.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = photo.description.takeIf { it.isNotBlank() } ?: "No description",
                modifier = Modifier.padding(8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun VideoItem(video: Video) {
    Card(
        modifier = Modifier
            .size(width = 180.dp, height = 160.dp)
    ) {
        Box {
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = video.description,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.singer)
            )

            // Play icon overlay
            Icon(
                painter = painterResource(
                    R.drawable.play_circle_outline
                ),
                contentDescription = "Play video",
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center),
                tint = Color.White.copy(alpha = 0.8f)
            )

            // Video description at the bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = video.description.takeIf { it.isNotBlank() } ?: "No description",
                    color = Color.White,
                    modifier = Modifier.padding(8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun Chip(
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    selected: Boolean
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (selected) Color(0xFFE8F0FE) else Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) colorResource(R.color.royal_blue) else Color.Gray.copy(alpha = 0.5f)
        )
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            label()
        }
    }
}