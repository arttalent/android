package com.example.talenta.presentation.myBookings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.talenta.R
import com.example.talenta.data.model.Booking
import com.example.talenta.data.model.BookingStatus
import com.example.talenta.data.model.PaymentStatus
import com.example.talenta.data.model.User
import com.example.talenta.data.model.getTitle
import com.example.talenta.ui.theme.TalentATheme
import com.example.talenta.ui.theme.green
import com.example.talenta.ui.theme.light_green
import com.example.talenta.ui.theme.light_red
import com.example.talenta.ui.theme.light_yellow
import com.example.talenta.ui.theme.red
import com.example.talenta.ui.theme.yellow

@Composable
fun MyBookingsScreen(
    viewModel: MyBookingsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchBookings()
    }
    val uiStates = viewModel.states.collectAsState().value
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
                    BookingCard(booking = booking, user = user)
                }
            }
        }
    }
}


@Composable
fun BookingCard(booking: Booking, user: User) {
    val selectedService = user.expertService?.find {
        it.serviceId == booking.serviceId
    }
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Booking ID
            Text(
                text = "Booking ID: ${booking.bookingId}",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // User info and rate
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Profile image
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = user.profilePicture),
                            contentDescription = "Profile Image of ${user.firstName}",
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Name and occupation
                    Column {
                        Text(
                            text = user.fullName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = user.professionalData.profession,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.Gray
                            )
                            Text(
                                text = user.professionalData.profession,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Rate
                Text(
                    text = "$${selectedService?.perHourCharge}/hr",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = colorResource(R.color.royal_blue)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Session info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Session type and payment status
                Row {
                    // Session type chip
                    Surface(
                        modifier = Modifier.padding(end = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF5F5F5)
                    ) {
                        Text(
                            text = selectedService?.serviceType?.getTitle() ?: "",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }

                    // Payment status chip
                    val (chipColor, textColor, statusText) = when (booking.paymentStatus) {
                        PaymentStatus.PAID -> Triple(Color(0xFFE9F7EF), Color(0xFF27AE60), "Paid")
                        PaymentStatus.NOT_PAID -> Triple(
                            Color(0xFFFAE9EF),
                            Color(0xFFE74C3C),
                            "Not Paid"
                        )

                        PaymentStatus.PENDING -> Triple(
                            Color(0xFFFFF9E6),
                            Color(0xFFF39C12),
                            "Pending"
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = chipColor
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(textColor)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = statusText,
                                fontSize = 12.sp,
                                color = textColor
                            )
                        }
                    }
                }

                // Action icons
                Row {
                    Icon(
                        painter = painterResource(R.drawable.document),
                        contentDescription = "Document",
                        modifier = Modifier.padding(end = 8.dp),
                        tint = Color.Gray
                    )
                    Icon(
                        painter = painterResource(R.drawable.view),
                        contentDescription = "View",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Time, date and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Time and date
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Time
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.star_outline),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${booking.timeInHrs} hrs",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    // Date
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.calendar),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = booking.scheduledStartTime,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Status chip
                val (chipColor, textColor, statusText) = when (booking.status) {
                    BookingStatus.ACCEPTED -> Triple(
                        light_green,
                        green,
                        "Accepted"
                    )

                    BookingStatus.REJECTED -> Triple(
                        light_red,
                        red,
                        "Rejected"
                    )

                    BookingStatus.RESCHEDULED -> Triple(
                        light_yellow,
                        yellow,
                        "Re-Scheduled"
                    )

                    BookingStatus.PENDING -> Triple(
                        light_yellow,
                        yellow,
                        "Pending"
                    )

                    BookingStatus.CONFIRMED -> Triple(
                        light_green,
                        green,
                        "Confirmed"
                    )

                    BookingStatus.COMPLETED -> Triple(
                        light_green,
                        green,
                        "Completed"
                    )

                    BookingStatus.CANCELLED -> Triple(
                        light_red,
                        red,
                        "Cancelled"
                    )

                    BookingStatus.UNATTENDED -> Triple(
                        light_yellow,
                        yellow,
                        "Unattended"
                    )
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = chipColor
                ) {
                    Text(
                        text = statusText,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        color = textColor
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    TalentATheme {
        MyBookingsScreen()
    }
}

