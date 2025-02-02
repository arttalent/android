package com.example.talenta.presentation.ui.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.talenta.R
import com.example.talenta.navigation.Routes.Route

@Composable
fun PasswordResetSuccessScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4169E1)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // You would typically use an actual icon resource here
        Icon(
            painter = painterResource(R.drawable.email_verification),
            contentDescription = "Success",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 32.dp)
        )

        Text(
            text = "Password created",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color.White
        )

        Text(
            text = "Your password has been created",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp),
            color = Color.White
        )

        Button(
            onClick = { navController.navigate(Route.Login.path) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            )
        ) {
            Text(
                "Back to Login",
                color = colorResource(R.color.royal_blue),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
