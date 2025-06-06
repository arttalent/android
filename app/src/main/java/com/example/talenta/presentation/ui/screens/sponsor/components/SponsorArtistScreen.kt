package com.example.talenta.presentation.ui.screens.sponsor.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.talenta.SponsorTemplateScreen
import com.example.talenta.navigation.Routes.Route
import com.example.talenta.presentation.ui.screens.sponsor.SponsorViewModel
import com.example.talenta.showMessage

@Composable
fun SponsorArtistScreen(
    navController: NavController? = null, sponsorViewModel: SponsorViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val artist = sponsorViewModel.artist.collectAsStateWithLifecycle().value
    val searchedUsers = sponsorViewModel.searchedUser.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            sponsorViewModel.search("firstName", searchQuery.trim())
        }
    }

    if (searchedUsers.isLoading || artist.isLoading) {
        CircularProgressIndicator()
    }
    LaunchedEffect(searchedUsers.error) {
//        showMessage(context, searchedUsers.error)
    }

    LaunchedEffect(artist.error) {
//        showMessage(context, artist.error)
    }


    val listOfArtist = if (searchQuery.isBlank()) {
        artist.user
    } else {
        searchedUsers.user.filter { it.isArtist == true }
    }

    SponsorTemplateScreen(
        user = listOfArtist,
        onSearchQueryChange = { newQuery -> searchQuery = newQuery },
        searchQuery = searchQuery,
        onExpertClick = { art ->
            navController?.navigate(Route.ExpertDetail(expert = art))
        },
        showSearch = true
    )
}


