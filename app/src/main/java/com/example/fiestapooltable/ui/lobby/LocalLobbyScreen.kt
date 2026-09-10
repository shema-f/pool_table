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
fun LocalLobbyScreen(
    onStartMatch: () -> Unit,
    onBack: () -> Unit
) {
    var localCode by remember { mutableStateOf("") }
    var inputCode by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("Create or join a nearby match") }

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
                text = "🤝 LOCAL MULTIPLAYER",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = FiestaGold
            )
            Text(
                text = "Play locally with a friend nearby",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Create Local Match Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "CREATE LOCAL MATCH", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = FiestaOrange)
                    Spacer(modifier = Modifier.height(12.dp))
                    if (localCode.isEmpty()) {
                        Button(
                            onClick = {
                                localCode = (Random.nextInt(900000) + 100000).toString()
                                statusMessage = "Match created! Code: $localCode. Waiting for friend..."
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FiestaOrange)
                        ) {
                            Text("Generate Join Code")
                        }
                    } else {
                        Text(text = "ROOM CODE", fontSize = 12.sp, color = Color.Gray)
                        Text(text = localCode, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = FiestaGold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onStartMatch,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                        ) {
                            Text("Start Local Match")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Join Local Match Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "JOIN LOCAL MATCH", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = FiestaGold)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = inputCode,
                        onValueChange = { inputCode = it },
                        label = { Text("Enter 6-digit Code") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (inputCode.length == 6) {
                                onStartMatch()
                            } else {
                                statusMessage = "Please enter a valid 6-digit code."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Join Match")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = statusMessage, fontSize = 14.sp, color = FiestaOrange)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
