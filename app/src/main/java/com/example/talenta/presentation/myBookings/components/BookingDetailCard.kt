package com.example.talenta.presentation.myBookings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.talenta.R
import com.example.talenta.data.model.Bio
import com.example.talenta.data.model.Booking
import com.example.talenta.data.model.BookingStatus
import com.example.talenta.data.model.ProfessionalData
import com.example.talenta.data.model.User
import com.example.talenta.data.model.getTitle

@Composable
fun BookingDetailCard(
    booking: Booking,
    user: User,
    onViewProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val selectedService = user.expertService?.find {
        it.serviceId == booking.serviceId
    }

    Column {
        // User profile card
        Card(
            modifier = modifier.fillMaxWidth().padding(10.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // User profile section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Profile image
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.LightGray)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = user.profilePicture),
                                contentDescription = "Profile Image of ${user.firstName}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.width(22.dp))

                        Column {
                            Text(
                                text = user.fullName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
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
                            // View Profile Button
                            TextButton(
                                onClick = onViewProfileClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4285F4)
                                ),
                                contentPadding = PaddingValues(
                                    horizontal = 10.dp,
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                            ) {
                                Text(
                                    text = "View Profile",
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }


                }
            }
        }

        Spacer(modifier = Modifier.padding(10.dp))
        // Booking Details Card
        Card(
            modifier = modifier.fillMaxWidth().padding(10.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                // Service details section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${selectedService?.perHourCharge ?: 25}/hr",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.calendar),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = booking.prettyStartDateTime(),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp), thickness = 1.dp
                )

                Column {
                    Text(
                        text = "Booking Details",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    DetailsRow(title = "Type of service : ") {
                        Text(
                            text = selectedService?.serviceType?.getTitle()?:"",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    DetailsRow(title = "Duration :") {
                        Text(
                            text = "${booking.timeInHrs} hrs",
                            fontSize = 14.sp,
                            color = Color.Gray,
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    DetailsRow(title = "Booking status :") {
                        Text(
                            text = booking.status.name,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    DetailsRow(title = "Payment Status :") {
                        Text(
                            text = booking.paymentStatus.toString(),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun DetailsRow(
    modifier: Modifier = Modifier,
    title: String = "Booking Details",
    Value: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
        Spacer(Modifier.padding(start = 2.dp))
        Value()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BookingDetailsCardPrev() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
    ) {
        BookingDetailCard(
            booking = Booking(
                bookingId = "12345",
                serviceId = "service_1",
                status = BookingStatus.PENDING,
                scheduledStartTime = "2023-10-01T10:00:00Z"
            ),
            user = User(
                id = "user_1",
                firstName = "John",
                lastName = "Doe",
                profilePicture = "https://example.com/profile.jpg",
                professionalData = ProfessionalData(
                    profession = "Software Engineer"
                ),
                bio = Bio(country = "USA")
            ),
        )
    }
}