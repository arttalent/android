package com.example.talenta.presentation.myBookings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import coil.compose.rememberAsyncImagePainter
import com.example.talenta.R
import com.example.talenta.data.model.Bio
import com.example.talenta.data.model.Booking
import com.example.talenta.data.model.DateSlot
import com.example.talenta.data.model.ExpertAvailability
import com.example.talenta.data.model.ProfessionalData
import com.example.talenta.data.model.Schedule
import com.example.talenta.data.model.Service
import com.example.talenta.data.model.ServiceType
import com.example.talenta.data.model.TimeSlot
import com.example.talenta.data.model.User
import com.example.talenta.data.model.getTitle
import com.example.talenta.utils.formatIsoToFormatterDateTime

@Composable
fun BookingListCard(
    booking: Booking,
    user: User,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {}
) {
    val selectedService = user.expertService?.find {
        it.serviceId == booking.serviceId
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onCardClick
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
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
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
                                text = user.bio.country,
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
                    PaymentStatusChip(paymentStatus = booking.paymentStatus)
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
                            text = formatIsoToFormatterDateTime(booking.scheduledStartTime),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Status chip
                BookingStatusChip(bookingStatus = booking.status)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookingListCardPreview() {
    BookingListCard(
        booking = Booking(
            bookingId = "B1234",
            expertId = "E1",
            artistId = "A1",
            serviceId = "S1",
            scheduledStartTime = "2025-06-01T10:00:00Z",
            timeInHrs = 2
        ),
        user = User(
            id = "U1",
            firstName = "John",
            lastName = "Doe",
            profilePicture = "",
            bio = Bio(country = "USA"),
            professionalData = ProfessionalData(profession = "Guitarist"),
            expertService = listOf(
                Service(
                    serviceId = "S1",
                    serviceType = ServiceType.TRAINING,
                    serviceTitle = ServiceType.TRAINING.getTitle(),
                    perHourCharge = 50f,
                    isActive = true,
                    expertAvailability = ExpertAvailability(
                        timezone = "Asia/Kolkata",
                        schedule = listOf(
                            Schedule(
                                DateSlot(
                                    startDateTime = "2025-06-01T10:00:00Z",
                                    endDateTime = "2025-06-01T12:00:00Z"
                                ), TimeSlot(
                                    start = "10:00",
                                    end = "12:00"
                                )
                            )
                        )
                    )

                )
            )
        )
    )
}

