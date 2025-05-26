package com.example.talenta.presentation.ui.screens.sponsor.components

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.talenta.SponsorTemplateScreen
import com.example.talenta.presentation.ui.screens.sponsor.SponsorViewModel

@Composable
fun SponsorArtistScreen(sponsorViewModel: SponsorViewModel = hiltViewModel()) {
    var sponsorArtist = sponsorViewModel.artist.collectAsStateWithLifecycle().value.user


    SponsorTemplateScreen(
        sponsorArtist, onSearchQueryChange = {}, "", {}, showSearch = true
    )
}