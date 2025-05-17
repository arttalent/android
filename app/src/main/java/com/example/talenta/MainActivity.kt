package com.example.talenta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.talenta.data.UserPreferences
import com.example.talenta.data.repository.AuthRepository
import com.example.talenta.navigation.AppNavigation
import com.example.talenta.ui.theme.TalentATheme
import com.example.talenta.utils.HelperFunctions.setScreenshotRestriction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferences: UserPreferences

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        actionBar?.hide()
        setScreenshotRestriction(this, false)

        setContent {

            TalentATheme {
                val isLoggedIn = authRepository.isUserLoggedIn()

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(isLoggedIn)
                }
            }
        }
    }

}

