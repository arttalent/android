package com.example.talenta.presentation.ui.screens.sponsor.components

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.talenta.SponsorTemplateScreen
import com.example.talenta.presentation.ui.screens.sponsor.SponsorViewModel


@Composable
fun SponsorExpertScreen(
    navController: NavController? = null,
    viewModel: SponsorViewModel = hiltViewModel()
) {
    var sponsorExpert = viewModel.expert.collectAsStateWithLifecycle().value.user

    SponsorTemplateScreen(
        sponsorExpert, onSearchQueryChange = {}, "", {}, showSearch = true
    )
}

