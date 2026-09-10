package com.example.fiestapooltable.ui.tournaments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun TournamentScreen(
    onBack: () -> Unit
) {
    var tournamentName by remember { mutableStateOf("Kigali Pool Masters") }
    var tournamentCode by remember { mutableStateOf("") }
    var joinedCode by remember { mutableStateOf("") }
    var isTournamentActive by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("Create or join a championship tournament") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FiestaDarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(onClick = onBack) {
                    Text("← Back")
                }
                Text(
                    text = "🏆 CHAMPIONSHIPS",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = FiestaGold
                )
                Spacer(modifier = Modifier.width(60.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!isTournamentActive) {
                // Create Championship Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "CREATE CHAMPIONSHIP", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = FiestaOrange)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = tournamentName,
                            onValueChange = { tournamentName = it },
                            label = { Text("Tournament Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        if (tournamentCode.isEmpty()) {
                            Button(
                                onClick = {
                                    val randomDigits = (Random.nextInt(900000) + 100000).toString()
                                    tournamentCode = "KPM-$randomDigits"
                                    isTournamentActive = true
                                    statusMessage = "Tournament $tournamentName created! Code: $tournamentCode"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = FiestaOrange),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Generate Championship Code")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Join Championship Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "JOIN CHAMPIONSHIP", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = FiestaGold)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = joinedCode,
                            onValueChange = { joinedCode = it },
                            label = { Text("Enter Code (e.g. KPM-482917)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                if (joinedCode.length >= 4) {
                                    isTournamentActive = true
                                    statusMessage = "Joined tournament $joinedCode successfully!"
                                } else {
                                    statusMessage = "Please enter a valid championship code."
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Join Tournament")
                        }
                    }
                }
            } else {
                // Live Tournament Bracket View
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = tournamentName.uppercase(), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = FiestaGold)
                        Text(text = "Code: ${if (tournamentCode.isNotEmpty()) tournamentCode else joinedCode}", fontSize = 12.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(text = "QUARTERFINALS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FiestaOrange)
                        Spacer(modifier = Modifier.height(8.dp))
                        BracketMatchItem("John ──────┐", "├── John (Winner)")
                        BracketMatchItem("Mike ──────┘", "│")
                        BracketMatchItem("Alex ──────┐", "├── Alex (Winner)")
                        BracketMatchItem("David ─────┘", "│")

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "SEMIFINALS & FINAL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FiestaOrange)
                        Spacer(modifier = Modifier.height(8.dp))
                        BracketMatchItem("John ──────┐", "├── 🏆 CHAMPION: ALEX")
                        BracketMatchItem("Alex ──────┘", "│")

                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { isTournamentActive = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                        ) {
                            Text("Leave Tournament")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = statusMessage, fontSize = 14.sp, color = FiestaOrange)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun BracketMatchItem(matchText: String, resultText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = matchText, fontSize = 13.sp, color = Color.White)
        Text(text = resultText, fontSize = 13.sp, color = FiestaGold, fontWeight = FontWeight.SemiBold)
    }
}
