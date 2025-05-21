package com.example.talenta.presentation.ui.screens.experts.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.talenta.R
import com.example.talenta.presentation.viewmodels.ExpertViewModel


@Composable
fun ProfileContent(expertId: String?, viewModel: ExpertViewModel = hiltViewModel()) {


    LaunchedEffect(expertId) {
        if (expertId != null) {
            viewModel.getExpertById(expertId)
        }
    }

    val expert by viewModel.expert.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "About Kieran", fontSize = 18.sp, fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (expert?.bio?.bioData.isNullOrBlank()) "No bio available" else expert?.bio?.bioData!!,
                    fontSize = 14.sp,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Musicianship", fontSize = 18.sp, fontWeight = FontWeight.Bold
                )


                Spacer(modifier = Modifier.height(8.dp))
                InfoSection(
                    "Genres: ", "Rock, Soul, Funk, Folk, Latin, Reggae, Gypsy Jazz, Classical"
                )
                InfoSection("Instruments: ", "Guitar, Singer (baritone), Singing pianist")
                (if (expert?.professionalData?.skills?.isEmpty() == true) "No skills added" else expert?.professionalData?.skills?.joinToString(
                    ", "
                ))?.let {
                    InfoSection(
                        "Other skills: ", it
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))


        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Awards", fontSize = 18.sp, fontWeight = FontWeight.Bold
                )
                Column {
                    val certificates = expert?.professionalData?.certificatesList

                    if (certificates.isNullOrEmpty()) {
                        AwardItem(
                            title = "No Certificate available",
                            subtitle = "Expert has not uploaded any certificates yet",
                            imageUrl = null
                        )
                    } else {
                        certificates.forEach { certificate ->
                            AwardItem(
                                title = certificate.description,
                                subtitle = certificate.description,
                                imageUrl = certificate.imageUrl
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }


            }
        }
    }
}

@Composable
fun InfoSection(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold
        )
        Text(
            text = content, fontSize = 14.sp
        )
    }
}

@Composable
private fun AwardItem(title: String, subtitle: String, imageUrl: String?) {

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
            AsyncImage(
                model = imageUrl,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.certificate),
                error = painterResource(id = R.drawable.caution),
                contentDescription = null,
                modifier = Modifier
                    .height(34.dp)
                    .width(50.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle, fontSize = 12.sp, color = Color.Gray
                )
            }
        }
    }
}
