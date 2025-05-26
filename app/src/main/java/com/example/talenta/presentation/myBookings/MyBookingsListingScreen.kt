package com.example.talenta.presentation.myBookings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.talenta.R
import com.example.talenta.presentation.myBookings.components.BookingListCard

@Composable
fun MyBookingsScreen(
    viewModel: MyBookingsViewModel = hiltViewModel()
) {
    val uiStates = viewModel.states.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "My Booking",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Search and filter row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Search field
            OutlinedTextField(
                value = "",
                onValueChange = { },
                placeholder = { Text("Search") },
                leadingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        DropdownMenu(
                            expanded = false,
                            onDismissRequest = {}
                        ) {
                            // Dropdown items would go here
                        }
                        Text(
                            "Booking ID",
                            modifier = Modifier.padding(end = 4.dp, start = 7.dp),
                            fontSize = 14.sp
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "search")
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            HorizontalDivider(
                modifier = Modifier
                    .height(24.dp)
                    .width(1.dp),
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.width(5.dp))

            // Filter button
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(48.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            ) {
                Icon(
                    painter = painterResource(R.drawable.filter),
                    contentDescription = "Filter",
                    tint = Color.Black
                )
            }
        }

        // Bookings list
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiStates.bookings) { booking ->
                // Find the user associated with the booking
                val isArtist = uiStates.currentUser?.isArtist
                val userId = if (isArtist == true) booking.expertId else booking.artistId
                val user = uiStates.users.find { it.id == userId }
                if (user != null) {
                    BookingListCard(
                        booking = booking,
                        user = user,
                        onCardClick = {}
                    )
                }
            }
        }
    }
}

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//private fun MyBookingsScreenPreiview() {
//    MyBookingsScreen()
//}
