package com.example.talenta.presentation.ui.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.talenta.R

data class Booking(
    val id: String,
    val name: String,
    val profileImage: Int, // Resource ID
    val occupation: String,
    val location: String,
    val sessionType: String,
    val paymentStatus: PaymentStatus,
    val duration: Int, // in minutes
    val date: String,
    val bookingStatus: BookingStatus,
    val hourlyRate: Int
)

enum class PaymentStatus {
    PAID, NOT_PAID, PENDING
}

enum class BookingStatus {
    ACCEPTED, REJECTED, RESCHEDULED
}

@Composable
fun MyBookingsScreen() {
    // Sample data
    val bookings = listOf(
        Booking(
            id = "123",
            name = "Kieran",
            profileImage = R.drawable.singer, // Replace with actual resource
            occupation = "Guitarist",
            location = "London, UK",
            sessionType = "Online Video Assessment",
            paymentStatus = PaymentStatus.PAID,
            duration = 60,
            date = "2 Jan 2025",
            bookingStatus = BookingStatus.ACCEPTED,
            hourlyRate = 25
        ),
        Booking(
            id = "125",
            name = "Samuel Moore",
            profileImage = R.drawable.singer, // Replace with actual resource
            occupation = "Guitarist",
            location = "London, UK",
            sessionType = "Online 1 on 1 Advise",
            paymentStatus = PaymentStatus.NOT_PAID,
            duration = 60,
            date = "23 Dec 2024",
            bookingStatus = BookingStatus.RESCHEDULED,
            hourlyRate = 25
        ),
        Booking(
            id = "126",
            name = "Samuel Moore",
            profileImage = R.drawable.singer, // Replace with actual resource
            occupation = "Guitarist",
            location = "London, UK",
            sessionType = "Online Live Assessment",
            paymentStatus = PaymentStatus.PENDING,
            duration = 30,
            date = "12 Dec 2024",
            bookingStatus = BookingStatus.REJECTED,
            hourlyRate = 25
        ),
        Booking(
            id = "123",
            name = "Kieran",
            profileImage = R.drawable.singer, // Replace with actual resource
            occupation = "Guitarist",
            location = "London, UK",
            sessionType = "Online Video Assessment",
            paymentStatus = PaymentStatus.PAID,
            duration = 60,
            date = "2 Jan 2025",
            bookingStatus = BookingStatus.ACCEPTED,
            hourlyRate = 25
        ),
        Booking(
            id = "125",
            name = "Samuel Moore",
            profileImage = R.drawable.singer, // Replace with actual resource
            occupation = "Guitarist",
            location = "London, UK",
            sessionType = "Online 1 on 1 Advise",
            paymentStatus = PaymentStatus.NOT_PAID,
            duration = 60,
            date = "23 Dec 2024",
            bookingStatus = BookingStatus.RESCHEDULED,
            hourlyRate = 25
        ),
        Booking(
            id = "126",
            name = "Samuel Moore",
            profileImage = R.drawable.singer, // Replace with actual resource
            occupation = "Guitarist",
            location = "London, UK",
            sessionType = "Online Live Assessment",
            paymentStatus = PaymentStatus.PENDING,
            duration = 30,
            date = "12 Dec 2024",
            bookingStatus = BookingStatus.REJECTED,
            hourlyRate = 25
        )
    )

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
            items(bookings) { booking ->
                BookingCard(booking = booking)
            }
        }
    }
}

@Composable
fun BookingCard(booking: Booking) {
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
                text = "Booking ID: ${booking.id}",
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
                        // Replace with actual image
                        Image(
                            painter = painterResource(R.drawable.singer),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Name and occupation
                    Column {
                        Text(
                            text = booking.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = booking.occupation,
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
                                text = booking.location,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Rate
                Text(
                    text = "$${booking.hourlyRate}/hr",
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
                            text = booking.sessionType,
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
                            text = "${booking.duration} min",
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
                            text = booking.date,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Status chip
                val (chipColor, textColor, statusText) = when (booking.bookingStatus) {
                    BookingStatus.ACCEPTED -> Triple(
                        Color(0xFFE9F7EF),
                        Color(0xFF27AE60),
                        "Accepted"
                    )

                    BookingStatus.REJECTED -> Triple(
                        Color(0xFFFAE9EF),
                        Color(0xFFE74C3C),
                        "Rejected"
                    )

                    BookingStatus.RESCHEDULED -> Triple(
                        Color(0xFFFFF9E6),
                        Color(0xFFF39C12),
                        "Re-Scheduled"
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
    MyBookingsScreen()
}

