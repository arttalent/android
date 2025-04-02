package com.example.talenta.presentation.ui.screens.experts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.talenta.R
import com.example.talenta.data.model.Expert
import com.example.talenta.presentation.viewmodels.ExpertViewModel


@Composable
fun ExpertsScreen(navController: NavController, viewModel: ExpertViewModel = hiltViewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    val experts by viewModel.experts.collectAsState(initial = emptyList())


    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.Gray,
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )


            // Experts Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(experts) { expert ->
                    ExpertCard(expert, navController)
                }

            }
        }
    }

}

@Composable
fun ExpertCard(expert: Expert, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                println("id from expert screen ${expert.id}")
                navController.navigate("expert_detail/${expert.id}")

            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        println("Details: $expert")
        Column(
            modifier = Modifier.padding(10.dp),
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image
            Image(
                painter = rememberAsyncImagePainter(if (expert.person.photoUrl.isNotEmpty()) expert.person.photoUrl else R.drawable.placeholder),
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(0.9f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
            Spacer(Modifier.height(5.dp))
            // Ratings & Followers
            Row {
                repeat(5) { index ->
                    Icon(
                        painter = painterResource(id = if (index < expert.rating) R.drawable.star_filled else R.drawable.star_outline),
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = "${expert.reviews} reviews",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(start = 4.dp)
            )
            Spacer(Modifier.height(5.dp))
            // Name & Location
            Text(
                text = expert.person.firstName + " " + expert.person.lastName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${expert.person.profession} | ${expert.location}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            Text(
                text = expert.profession,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}

