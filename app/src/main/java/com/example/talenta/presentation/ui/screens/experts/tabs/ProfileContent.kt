package com.example.talenta.presentation.ui.screens.experts.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talenta.R


@Composable
  fun ProfileContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "About Kieran",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "I am a professional guitarist and qualified music teacher with extensive experience as a session instrumentalist, and band member across various genres including Rock, Soul, Funk, Folk, Latin, Reggae, Gypsy Jazz, Classical and more.",
                    fontSize = 14.sp,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Musicianship",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))
                InfoSection(
                    "Genres: ",
                    "Rock, Soul, Funk, Folk, Latin, Reggae, Gypsy Jazz, Classical"
                )
                InfoSection("Instruments: ", "Guitar, Singer (baritone), Singing pianist")
                InfoSection(
                    "Other skills: ",
                    "Theory teaching, Instrumental teaching, Musicologist, Perfect pitch"
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))


        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Awards",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                AwardItem(
                    title = "Certificate of excellence",
                    subtitle = "From Yale School of Music"
                )
                Spacer(modifier = Modifier.height(6.dp))
                AwardItem(
                    title = "Songwriting: Writing the Music",
                    subtitle = "From Yale School of Music"
                )
            }
        }
    }
}

@Composable
fun InfoSection(title: String, content: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 2.dp)) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = content,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun AwardItem(title: String, subtitle: String) {

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.certificate),
                contentDescription = null,
                modifier = Modifier
                    .height(34.dp)
                    .width(50.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
