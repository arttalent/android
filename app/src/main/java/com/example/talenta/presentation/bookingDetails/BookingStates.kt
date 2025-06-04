package com.example.talenta.presentation.bookingDetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.talenta.data.model.BookingStatus

@Composable
fun BookingStates(uiState: BookingDetailsState, modifier: Modifier = Modifier) {
    val currentUser = uiState.currentUser
    val booking = uiState.booking
    val user = uiState.user

    when{
        booking?.status == BookingStatus.PENDING -> {
        }
    }
}

@Composable
fun BookingStateForPendingArtist(modifier: Modifier = Modifier) {

}

