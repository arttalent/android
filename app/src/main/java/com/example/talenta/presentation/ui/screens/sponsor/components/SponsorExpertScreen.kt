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
fun SponsorExpertScreen(
    navController: NavController? = null, sponsorViewModel: SponsorViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val expert = sponsorViewModel.expert.collectAsStateWithLifecycle().value
    val searchedUsers = sponsorViewModel.searchedUser.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            sponsorViewModel.search("firstName", searchQuery.trim())
        }
    }

    if (searchedUsers.isLoading || expert.isLoading) {
        CircularProgressIndicator()
    }
    LaunchedEffect(searchedUsers.error) {
        showMessage(context, searchedUsers.error)
    }

    LaunchedEffect(expert.error) {
        showMessage(context, expert.error)
    }


    val listOfExpert = if (searchQuery.isBlank()) {
        expert.user
    } else {
        searchedUsers.user.filter { it.isExpert == true }
    }

    SponsorTemplateScreen(
        user = listOfExpert,
        onSearchQueryChange = { newQuery -> searchQuery = newQuery },
        searchQuery = searchQuery,
        onExpertClick = { exp ->
            navController?.navigate(Route.ExpertDetail(expert = exp))
        },
        showSearch = true
    )
}

