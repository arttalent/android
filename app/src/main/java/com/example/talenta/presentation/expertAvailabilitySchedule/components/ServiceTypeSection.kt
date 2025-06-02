package com.example.talenta.presentation.expertAvailabilitySchedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.talenta.data.model.ServiceType
import com.example.talenta.data.model.getTitle

@Composable
fun ServiceTypeSection(
    selectedServiceType: ServiceType?,
    onServiceTypeSelected: (ServiceType) -> Unit
) {
    val serviceTypes = ServiceType.entries
    var dropdownExpanded by remember { mutableStateOf(false) }

    Text(
        text = "Type of service",
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable { dropdownExpanded = true }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = selectedServiceType?.getTitle() ?: "Select the type of service",
            color = if (selectedServiceType == null) Color.Gray else Color.Black
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Dropdown",
            tint = Color.Gray,
            modifier = Modifier.align(Alignment.CenterEnd)
        )

        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false },
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .background(Color.White),
            properties = PopupProperties(focusable = true)
        ) {
            serviceTypes.forEach { serviceType ->
                DropdownMenuItem(
                    text = { Text(serviceType.getTitle()) },
                    onClick = {
                        onServiceTypeSelected(serviceType)
                        dropdownExpanded = false
                    }
                )
            }
        }
    }
}