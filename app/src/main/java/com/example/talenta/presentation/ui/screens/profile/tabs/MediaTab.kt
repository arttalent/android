package com.example.talenta.presentation.ui.screens.profile.tabs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImage
import com.example.talenta.R
import com.example.talenta.data.model.Media
import com.example.talenta.data.model.MediaType
import com.example.talenta.presentation.ui.screens.Fab
import com.example.talenta.presentation.ui.screens.profile.AddMediaDialog
import com.example.talenta.presentation.viewmodels.ArtistProfileViewModel
import com.example.talenta.presentation.viewmodels.UploadMediaState

@Composable
fun MediaContent(
    viewModel: ArtistProfileViewModel,
) {
    val uiStates by viewModel.profileState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    Box {
        Fab(modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp)) {
            showDialog = true
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Add media button
            if (showDialog) {
                AddMediaDialog(
                    onDismiss = { showDialog = false },
                    viewModel = viewModel
                )
            }
            // All Media Section
            val photos = uiStates.user?.professionalData?.media?.filter {
                it.type == MediaType.IMAGE
            } ?: emptyList()
            val videos = uiStates.user?.professionalData?.media?.filter {
                it.type == MediaType.VIDEO
            } ?: emptyList()

            if (photos.isNotEmpty()) {
                Text(
                    text = "Photos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(photos) { mediaItem ->
                        PhotoItem(photo = mediaItem)
                    }
                }
            }

            if (videos.isNotEmpty()) {
                Text(
                    text = "Videos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(videos) { mediaItem ->
                        VideoItem(video = mediaItem)
                    }
                }
            }

            // Show upload status
            val uploadMediaState = uiStates.uploadMediaState
            when (uploadMediaState) {
                is UploadMediaState.Loading -> {
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

                is UploadMediaState.Error -> {
                    Text(
                        text = "Upload failed: ${uploadMediaState.message}",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                else -> {} // No indication needed for Success or Idle states
            }
        }
    }

}

@Composable
fun SeeAllButton(modifier: Modifier = Modifier) {
    /*          // See all button
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
          }*/
}

@Composable
private fun PhotoItem(photo: Media) {
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
                placeholder = painterResource(id = R.drawable.singer)
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
private fun VideoItem(video: Media) {
    // State to control video playback
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
                    painter = painterResource(
                        R.drawable.play_circle_outline
                    ),
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