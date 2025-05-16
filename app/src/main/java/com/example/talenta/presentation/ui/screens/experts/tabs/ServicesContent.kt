package com.example.talenta.presentation.ui.screens.experts.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.talenta.R
import com.example.talenta.navigation.Routes.Route.Routes
import timber.log.Timber


data class Service(
    val title: String, val price: String, val features: List<String>, val id: String = ""
)


@Composable
fun ServicesContent(navController: NavController, expertId: String) {
    val services = listOf(
        Service("Online Video Assessment", "$25/hr", listOf("Video Assessment", "Report"), "video"),
        Service("Online Live Assessment", "$25/hr", listOf("Live Assessment", "Report"), "live"),
        Service("Online 1 on 1 Advise", "$25/hr", listOf("1 on 1 Advise", "Doubts"), "advise")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        services.forEachIndexed { index, service ->
            ServiceCard(
                title = service.title,
                price = service.price,
                features = service.features,
                onBookingClick = {
                    navController.navigate(Routes.withArgs(expertId, service.id))
                    Timber.tag("ServiceCard").d("Booking clicked for ${service.title}")
                })
            if (index < services.lastIndex) {
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}


@Composable
private fun ServiceCard(
    title: String, price: String, features: List<String>, onBookingClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp
                )
                Text(
                    text = price,
                    color = colorResource(R.color.royal_blue),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = feature, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onBookingClick()
                }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.royal_blue),
                )
            ) {
                Text("Book Now")
            }
        }
    }
}

