package com.example.talenta.ui.auth.signup

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.talenta.R
import com.example.talenta.navigation.Routes.Route

@Composable
fun SuccessScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4169E1)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 40.dp)
        ) {
            // You can replace this with your actual image
            Box(
                modifier = Modifier
                    .size(120.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.email_verification),
                    contentDescription = ""
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Email Verified",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your email address has been verified successfully and your account has been created.",
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate(Route.Home.path) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Continue",
                    color = Color(0xFF4169E1)
                )
            }
        }
    }
}
