package com.example.talenta.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class OnboardingPage(
    @DrawableRes val image: Int,
    val title: String,
    val description: String,
    val backgroundColor: Color
)
