package com.example.talenta.jitsi

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

@Composable
fun jitsi() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var roomName by remember { mutableStateOf("") }

    // Initialize Jitsi configuration when the composable is first created
    LaunchedEffect(Unit) {
        val serverURL = URL("https://meet.ffmuc.net")
        val defaultOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(serverURL)
            .setFeatureFlag("pip.enabled", true)
            .setFeatureFlag("welcomepage.enabled", false)
            .setFeatureFlag("prejoinpage.enabled", false)
            .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true }
        ) {
            Text("Start Video Call")
        }
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Enter Room Name",
                        style = MaterialTheme.typography.titleLarge
                    )

                    OutlinedTextField(
                        value = roomName,
                        onValueChange = { roomName = it },
                        label = { Text("Room Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { showDialog = false }
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (roomName.isNotEmpty()) {
                                    val options = JitsiMeetConferenceOptions.Builder()
                                        .setRoom(roomName)
                                        .setFeatureFlag("prejoinpage.enabled", false)
                                        .build()
                                    JitsiMeetActivity.launch(context, options)
                                    showDialog = false
                                    roomName = ""
                                }
                            }
                        ) {
                            Text("Join")
                        }
                    }
                }
            }
        }
    }
}