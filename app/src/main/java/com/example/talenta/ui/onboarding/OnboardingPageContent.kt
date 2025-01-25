package com.example.talenta.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.abs


@Composable
fun OnboardingPageContent(
    page: OnboardingPage,
    offset: Float
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(page.backgroundColor)
            .padding(24.dp)
            .graphicsLayer {
                // Add parallax effect to the content
                translationX = size.width * offset * 0.2f
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = page.image),
                contentDescription = null,
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(16.dp)
                    .graphicsLayer {
                        // Add a slight rotation effect based on the scroll
                        rotationZ = offset * 8f
                        // Add scale effect
                        scaleX = 1f - abs(offset) * 0.1f
                        scaleY = 1f - abs(offset) * 0.1f
                    },
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = page.title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .graphicsLayer {
                        // Slide in text effect
                        translationX = size.width * offset * 0.4f
                        alpha = 1f - abs(offset)
                    }
            )

            Text(
                text = page.description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                ),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .graphicsLayer {
                        // Delayed slide in effect for description
                        translationX = size.width * offset * 0.6f
                        alpha = 1f - abs(offset)
                    }
            )
        }
    }
}