package com.example.talenta.presentation.ui.screens.profile.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.talenta.data.model.Service
import com.example.talenta.data.model.ServiceType
import com.example.talenta.data.model.User
import com.example.talenta.data.model.getTitle
import com.example.talenta.presentation.ui.screens.Fab
import com.example.talenta.ui.theme.TalentATheme

@Composable
fun ServiceTab(
    user: User,
    navigateToCreateService: () -> Unit = { /* No-op */ }
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            user.expertService?.forEach { service ->
                ExpertProfileServiceCard(service = service)
            }
        }

        Fab(
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            navigateToCreateService()
        }

    }

}


@Composable
fun ExpertProfileServiceCard(modifier: Modifier = Modifier, service: Service) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceAround
            ) {
                Text(
                    text = service.serviceType?.getTitle() ?: "",
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = service.perHourCharge.toString() + "$",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                )

            }

            Spacer(modifier = Modifier.padding(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { /* Handle booking */ },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Delete",
                        color = Color.White
                    )
                }
                Button(
                    onClick = { /* Handle booking */ },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Edit",
                        color = Color.White
                    )
                }

            }
        }
    }

}

@Preview
@Composable
private fun ExpertProfileServiceCardPReview() {
    TalentATheme {
        ExpertProfileServiceCard(
            service = Service(
                serviceId = "1123",
                serviceTitle = ServiceType.LIVE_ASSESSMENT.getTitle(),
                perHourCharge = 50.0f,
                serviceType = ServiceType.LIVE_ASSESSMENT,
            )
        )
    }
}