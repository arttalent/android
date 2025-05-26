package com.example.talenta.presentation.expertBooking

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.talenta.R
import com.example.talenta.ui.theme.TalentATheme

@Composable
fun BookingBottomSheet(
    modifier: Modifier,
    expertName: String = "Kieran",
    time: String = "02:30pm - 03:30pm",
    date: String = "Thursday, April 10th",
    fees: String = "$25",
    loading: Boolean = false,
    onConfirmClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        tonalElevation = 8.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Online 1 on 1 Advise",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Expert Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_stethoscope),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp)),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = expertName, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Time Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_calendarblank),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "$time,\n$date", color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Fees Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_perhrcost),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Fees: $fees", color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Button
            Button(
                onClick = onConfirmClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = "Confirm")
                }
            }
        }
    }
}

@Preview
@Composable
private fun BookingBottomSheetPrev() {
    TalentATheme {
        BookingBottomSheet(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            expertName = "Kieran",
            time = "02:30pm - 03:30pm",
            date = "Thursday, April 10th",
            fees = "$25"
        )
    }
}
