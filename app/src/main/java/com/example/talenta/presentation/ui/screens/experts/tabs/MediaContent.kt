package com.example.talenta.presentation.ui.screens.experts.tabs

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talenta.R


@Composable
fun MediaContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp).verticalScroll(rememberScrollState())
    ) {
        // Media type filters
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(listOf("All", "Photos", "Videos")) { filter ->
                Chip(
                    onClick = { /* Handle filter selection */ },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (filter != "All") {
                                Icon(
                                    painter = if (filter == "Photos") painterResource(R.drawable.photo_camera) else painterResource(
                                        R.drawable.videocam
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(text = filter)
                        }
                    },
                    selected = filter == "All"
                )
            }
        }

        // Photos Section
        Text(
            text = "Photos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(2) {
                OutlinedCard (
                    modifier = Modifier
                        .size(width = 180.dp, height = 160.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)

                ) {
                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.singer),
                            contentDescription = "Professional photo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "I am a professional .",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "See more",
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
        )

        // Videos Section
        Text(
            text = "Videos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(2) {
                Card(
                    modifier = Modifier
                        .size(width = 180.dp, height = 160.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.singer),
                        contentDescription = "Video thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "See more",
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
        )
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
        color = if (selected) Color(0xFFE8F0FE) else Color.Transparent
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            label()
        }
    }
}