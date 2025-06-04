package com.example.talenta.presentation.bookingDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.talenta.data.model.LocalBooking
import com.example.talenta.data.model.User
import com.example.talenta.ui.theme.TalentATheme
import com.example.talenta.utils.FakeModels

@Composable
fun BookingDetails(
    localBooking: LocalBooking,
    onBackClick: () -> Unit,
    onProfileClick: (User) -> Unit
) {
    val viewmodel = hiltViewModel<BookingDetailsViewmodel>()
    val uiStates = viewmodel.uiStates.collectAsStateWithLifecycle()
    LaunchedEffect(
        key1 = Unit
    ) {
        viewmodel.onAction(
            BookingDetailsActions.InitData(
                booking = localBooking.booking,
                expertDetails = localBooking.expertDetails?: User(),
                artistDetails = localBooking.artistDetails?: User()
            )
        )
    }

    BookingDetailScreen(
        uiStates = uiStates.value,
        onActions = viewmodel::onAction,
        onBackClick = onBackClick,
        onViewProfileClick = onProfileClick
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailScreen(
    uiStates: BookingDetailsState,
    onActions: (BookingDetailsActions) -> Unit,
    onBackClick: () -> Unit = {},
    onViewProfileClick: (User) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val user = uiStates.user ?: return@Column
            val booking = uiStates.booking ?: return@Column
            BookingDetailCard(
                booking = booking,
                user = user,
                onViewProfileClick = {
                    onViewProfileClick(user)
                }
            )


        }
    }
}

@Preview
@Composable
private fun BookingDetailsPrev() {
    TalentATheme {
        BookingDetailScreen(
            uiStates = BookingDetailsState(
                booking = FakeModels.fakeBooking,
                user = FakeModels.fakeExpertUser,
                currentUser = FakeModels.fakeUserArtist
            ),
            onActions = {},
            onBackClick = {},
            onViewProfileClick = {}
        )
    }
}
