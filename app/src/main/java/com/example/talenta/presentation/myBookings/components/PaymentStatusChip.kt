package com.example.talenta.presentation.myBookings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talenta.data.model.BookingStatus
import com.example.talenta.data.model.PaymentStatus
import com.example.talenta.ui.theme.green
import com.example.talenta.ui.theme.light_green
import com.example.talenta.ui.theme.light_red
import com.example.talenta.ui.theme.light_yellow
import com.example.talenta.ui.theme.red
import com.example.talenta.ui.theme.yellow

@Composable
fun PaymentStatusChip(
    paymentStatus: PaymentStatus,
    modifier: Modifier = Modifier
) {
    val (chipColor, textColor, statusText) = when (paymentStatus) {
        PaymentStatus.PAID -> Triple(
            Color(0xFFE9F7EF),
            Color(0xFF27AE60),
            "Paid"
        )
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
        color = chipColor,
        modifier = modifier
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

@Composable
fun BookingStatusChip(
    bookingStatus: BookingStatus,
    modifier: Modifier = Modifier
) {
    val (chipColor, textColor, statusText) = when (bookingStatus) {
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
        color = chipColor,
        modifier = modifier
    ) {
        Text(
            text = statusText,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            color = textColor
        )
    }
}