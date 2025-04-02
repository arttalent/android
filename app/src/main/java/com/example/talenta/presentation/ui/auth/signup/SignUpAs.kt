package com.example.talenta.presentation.ui.auth.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.talenta.R
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.viewmodels.SignInViewModel
import com.example.talenta.ui.theme.TalentATheme

data class CardItem(
    val id: Int,
    val title: String,
    val imageRes: Int
)




@Composable
fun SignUpAs(
    navController: NavController,
) {
    val cardItems = listOf(
        CardItem(1, "Experts", R.drawable.experts),
        CardItem(2, "Artists", R.drawable.artist),
        CardItem(3, "Sponsors", R.drawable.sponsors),
        CardItem(4, "Fan or Follower", R.drawable.fan)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top section with icon and title
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.groups),
                    contentDescription = "Choose Role",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Choose Your Role",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Select how you want to participate in our creative community",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(cardItems) { item ->
                    GridCard(
                        cardItem = item,
                        onCardClick = {
                            navController.navigate(Route.SignUp.path)
                        }
                    )
                }
            }

            // Bottom section with back button
            TextButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Icon(
                    Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Back to Welcome Screen")
            }
        }
    }
}

@Composable
fun GridCard(modifier: Modifier = Modifier, cardItem: CardItem, onCardClick: () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                        )
                    )
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Role icon based on type
                Icon(
                    painter = painterResource(
                        when (cardItem.title) {
                            "Experts" -> R.drawable.experts
                            "Artists" -> R.drawable.artist
                            "Sponsors" -> R.drawable.sponsors
                            else -> R.drawable.fan
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(bottom = 8.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = cardItem.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Role description
                Text(
                    text = when (cardItem.title) {
                        "Experts" -> "Share your knowledge"
                        "Artists" -> "Showcase your talent"
                        "Sponsors" -> "Support creativity"
                        else -> "Join the community"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun GridCardPrev() {
    TalentATheme {
        GridCard(
            modifier = Modifier
                .fillMaxSize(0.5f)
                .padding(20.dp),
            cardItem = CardItem(1, "Experts", R.drawable.experts),
        ) { }
    }
}

@Preview
@Composable
private fun SignUpAsPrev() {
    TalentATheme {
        SignUpAs(
            navController = NavController(context = LocalContext.current)
        )
    }
}