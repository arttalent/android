package com.example.talenta.presentation.ui.screens.experts.tabs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.talenta.R
import com.example.talenta.data.model.Media
import com.example.talenta.data.model.MediaType
import com.example.talenta.presentation.viewmodels.ExpertViewModel
import com.example.talenta.presentation.viewmodels.MediaFilter

@Composable
fun MediaTabForExpert(
) {
    val viewModel: ExpertViewModel = hiltViewModel()
    val uiState by viewModel.profileState.collectAsState()

    // Show loading indicator
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = colorResource(R.color.royal_blue)
            )
        }
        return
    }

    // Show error if any
    uiState.error?.let { error ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Media type filters
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(listOf(
                MediaFilter.ALL to "All",
                MediaFilter.PHOTOS to "Photos",
                MediaFilter.VIDEOS to "Videos"
            )) { (filter, label) ->
                Chip(
                    onClick = { viewModel.onMediaFilterSelected(filter) },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (filter != MediaFilter.ALL) {
                                Icon(
                                    painter = if (filter == MediaFilter.PHOTOS)
                                        painterResource(R.drawable.photo_camera)
                                    else
                                        painterResource(R.drawable.videocam),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(text = label)
                        }
                    },
                    selected = uiState.selectedMediaFilter == filter
                )
            }
        }

        // Get filtered media based on selection
        val allMedia = uiState.expert?.professionalData?.media ?: emptyList()
        val photos = allMedia.filter { it.type == MediaType.IMAGE }
        val videos = allMedia.filter { it.type == MediaType.VIDEO }

        val displayPhotos = when (uiState.selectedMediaFilter) {
            MediaFilter.ALL, MediaFilter.PHOTOS -> photos
            MediaFilter.VIDEOS -> emptyList()
        }

        val displayVideos = when (uiState.selectedMediaFilter) {
            MediaFilter.ALL, MediaFilter.VIDEOS -> videos
            MediaFilter.PHOTOS -> emptyList()
        }

        // Photos Section
        if (displayPhotos.isNotEmpty()) {
            Text(
                text = "Photos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(displayPhotos) { photo ->
                    ExpertPhotoItem(photo = photo)
                }
            }

            // See more arrow for photos
            if (displayPhotos.size > 2) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "See more photos",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                        .clickable { /* Handle see more photos */ }
                )
            }
        }

        // Videos Section
        if (displayVideos.isNotEmpty()) {
            Text(
                text = "Videos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(displayVideos) { video ->
                    ExpertVideoItem(video = video)
                }
            }

            // See more arrow for videos
            if (displayVideos.size > 2) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "See more videos",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                        .clickable { /* Handle see more videos */ }
                )
            }
        }

        // Show message when no media available
        if (allMedia.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No media available",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ExpertPhotoItem(photo: Media) {
    OutlinedCard(
        modifier = Modifier
            .size(width = 180.dp, height = 160.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = photo.url,
                contentDescription = photo.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.singer),
                error = painterResource(id = R.drawable.singer)
            )
            Text(
                text = photo.description.takeIf { it.isNotBlank() } ?: "Professional photo",
                modifier = Modifier.padding(8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ExpertVideoItem(video: Media) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var videoViewRef by remember { mutableStateOf<android.widget.VideoView?>(null) }

    Card(
        modifier = Modifier
            .size(width = 180.dp, height = 160.dp)
    ) {
        Box {
            androidx.compose.ui.viewinterop.AndroidView(
                factory = { ctx ->
                    android.widget.VideoView(ctx).apply {
                        setVideoPath(video.url)
                        setOnPreparedListener { it.isLooping = true }
                        videoViewRef = this
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { view ->
                    if (isPlaying) {
                        view.start()
                    } else {
                        view.pause()
                        view.seekTo(0)
                    }
                }
            )

            // Play icon overlay (shows only when not playing)
            if (!isPlaying) {
                Icon(
                    painter = painterResource(R.drawable.play_circle_outline),
                    contentDescription = "Play video",
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                        .clickable {
                            isPlaying = true
                        },
                    tint = Color.White.copy(alpha = 0.8f)
                )
            }

            // Video description at the bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = video.description.takeIf { it.isNotBlank() } ?: "Professional video",
                    color = Color.White,
                    modifier = Modifier.padding(8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp
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