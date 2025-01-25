package com.example.talenta.ui.auth.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.talenta.R
import com.example.talenta.navigation.Routes.Route

data class CardItem(
    val id: Int,
    val title: String,
    val imageRes: Int
)

@Composable
fun SignUpAs(navController: NavController) {

    val cardItems = listOf(
        CardItem(1, "Exports", R.drawable.experts),
        CardItem(2, "Artists", R.drawable.artist),
        CardItem(3, "Sponsors", R.drawable.sponsors),
        CardItem(4, "Fan or Follower", R.drawable.fan)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign Up as",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.CenterHorizontally)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
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
    }
}

@Composable
fun GridCard(cardItem: CardItem, onCardClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(7.dp)
            .clickable(onClick = onCardClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.light_blue))
        ) {

            Text(
                text = cardItem.title,
                fontSize = 16.sp,
                color = colorResource(R.color.royal_blue),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(8.dp)
            )

            Image(
                painter = painterResource(id = cardItem.imageRes),
                contentDescription = cardItem.title,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp)
                    .weight(1f),
                contentScale = ContentScale.Fit
            )
        }
    }
}