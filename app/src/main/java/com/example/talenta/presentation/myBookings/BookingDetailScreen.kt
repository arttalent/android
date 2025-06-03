package com.example.talenta.presentation.myBookings

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.talenta.data.model.Booking
import com.example.talenta.data.model.LocalBooking
import com.example.talenta.data.model.User
import com.example.talenta.presentation.myBookings.components.BookingDetailCard
import com.example.talenta.ui.theme.TalentATheme
import com.example.talenta.utils.FakeModels

@Composable
fun BookingDetails(modifier: Modifier = Modifier,localBooking: LocalBooking) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailScreen(
    booking: Booking,
    user: User,
    showAcceptReject: Boolean = false,
    onBackClick: () -> Unit = {},
    onAcceptClick: () -> Unit = {},
    onRejectClick: () -> Unit = {},
    onViewProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
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
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BookingDetailCard(
                booking = booking,
                user = user,
                onViewProfileClick = onViewProfileClick
            )


        }
    }
}

@Preview
@Composable
private fun BookingDetailsPrev() {
    TalentATheme {
        BookingDetailScreen(
            booking = FakeModels.fakeBooking,
            user = FakeModels.fakeExpertUser,
            showAcceptReject = true,
            onBackClick = {},
            onAcceptClick = {},
            onRejectClick = {},
            onViewProfileClick = {}
        )
    }
}
