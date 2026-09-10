package com.example.fiestapooltable.ui.lobby

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fiestapooltable.ui.theme.FiestaDarkBackground
import com.example.fiestapooltable.ui.theme.FiestaGold
import com.example.fiestapooltable.ui.theme.FiestaOrange
import kotlin.random.Random

@Composable
fun OnlineLobbyScreen(
    onStartMatch: () -> Unit,
    onBack: () -> Unit
) {
    var roomCode by remember { mutableStateOf("") }
    var inputRoom by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("Create or join an online private room") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FiestaDarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedButton(onClick = onBack, modifier = Modifier.align(Alignment.Start)) {
                Text("← Back")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "🌐 ONLINE PRIVATE MATCH",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = FiestaGold
            )
            Text(
                text = "Play securely online using temporary verification codes",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Create Online Room Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "CREATE ONLINE ROOM", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = FiestaOrange)
                    Spacer(modifier = Modifier.height(12.dp))
                    if (roomCode.isEmpty()) {
                        Button(
                            onClick = {
                                val randomDigits = (Random.nextInt(900000) + 100000).toString()
                                roomCode = "FIESTA-$randomDigits"
                                statusMessage = "Room created: $roomCode. Waiting for opponent..."
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FiestaOrange)
                        ) {
                            Text("Generate Room Code")
                        }
                    } else {
                        Text(text = "SECURE ROOM CODE", fontSize = 12.sp, color = Color.Gray)
                        Text(text = roomCode, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = FiestaGold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onStartMatch,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                        ) {
                            Text("Start Online Match")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Join Online Room Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "JOIN ONLINE ROOM", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = FiestaGold)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = inputRoom,
                        onValueChange = { inputRoom = it },
                        label = { Text("Enter Room Code (e.g. FIESTA-482917)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (inputRoom.startsWith("FIESTA-") && inputRoom.length == 13) {
                                onStartMatch()
                            } else {
                                statusMessage = "Please enter a valid room code format (FIESTA-XXXXXX)."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Connect to Room")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = statusMessage, fontSize = 14.sp, color = FiestaOrange)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
