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
import com.example.talenta.R
import com.example.talenta.data.model.Services
import java.util.UUID


@Composable
fun ServicesContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ServiceCard(
            services = Services(
                serviceId = UUID.randomUUID().toString(),
                title = "Online Video Call",
                price = "$25/hr",
                features = listOf("Video Call", "Report")
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        ServiceCard(
            services = Services(
                serviceId = UUID.randomUUID().toString(),
                title = "Online Text Assessment",
                price = "$25/hr",
                features = listOf("Text Assessment", "Report")
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        ServiceCard(
            services = Services(
                serviceId = UUID.randomUUID().toString(),
                title = "Online 1 on 1 Advise",
                price = "$25/hr",
                features = listOf("1 on 1 Advise", "Doubts")
            )
        )
    }
}

@Composable
private fun ServiceCard(
    services: Services
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
                    text = services.title, fontWeight = FontWeight.Bold, fontSize = 16.sp
                )
                Text(
                    text = services.price,
                    color = colorResource(R.color.royal_blue),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            services.features.forEach { feature ->
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
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.royal_blue),
                )
            ) {
                Text("Book Now")
            }
        }
    }
}

