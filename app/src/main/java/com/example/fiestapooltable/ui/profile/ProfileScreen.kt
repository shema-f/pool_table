package com.example.fiestapooltable.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fiestapooltable.PlayerStats
import com.example.fiestapooltable.User
import com.example.fiestapooltable.ui.theme.FiestaDarkBackground
import com.example.fiestapooltable.ui.theme.FiestaGold
import com.example.fiestapooltable.ui.theme.FiestaGreenDark
import com.example.fiestapooltable.ui.theme.FiestaGreenFelt
import com.example.fiestapooltable.ui.theme.FiestaOrange

@Composable
fun ProfileScreen(
    onBack: () -> Unit
) {
    // Sample mock profile data matching product vision
    val user = User(
        userId = "user_001",
        username = "Alex",
        avatarUrl = "",
        level = 27,
        xp = 14250,
        coins = 3400,
        trophies = 1850
    )

    val stats = PlayerStats(
        userId = "user_001",
        gamesPlayed = 284,
        wins = 196,
        losses = 88,
        winRate = 69f,
        tournamentsPlayed = 14,
        tournamentsWon = 5,
        aggression = 8.0f,
        defense = 5.0f,
        trickShots = 9.0f,
        risk = 7.0f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FiestaDarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(onClick = onBack) {
                    Text("← Back")
                }
                Text(
                    text = "FIESTA PROFILE",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = FiestaGold
                )
                Spacer(modifier = Modifier.width(60.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Avatar & User Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(FiestaOrange, FiestaGold)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.username.take(1).uppercase(),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = user.username,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Level ${user.level} • 🏆 ${user.trophies} Trophies",
                        fontSize = 14.sp,
                        color = FiestaGold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatBadge(label = "Games", value = "${stats.gamesPlayed}")
                        StatBadge(label = "Wins", value = "${stats.wins}")
                        StatBadge(label = "Win Rate", value = "${stats.winRate.toInt()}%")
                        StatBadge(label = "Tourneys", value = "${stats.tournamentsWon}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Play Style Statistics Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "PLAY STYLE PROFILE",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = FiestaOrange
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PlayStyleBar(label = "Aggression", value = stats.aggression)
                    Spacer(modifier = Modifier.height(12.dp))
                    PlayStyleBar(label = "Defense", value = stats.defense)
                    Spacer(modifier = Modifier.height(12.dp))
                    PlayStyleBar(label = "Trick Shots", value = stats.trickShots)
                    Spacer(modifier = Modifier.height(12.dp))
                    PlayStyleBar(label = "Risk", value = stats.risk)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StatBadge(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun PlayStyleBar(label: String, value: Float) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 14.sp, color = Color.White)
            Text(text = "${value.toInt()}/10", fontSize = 14.sp, color = FiestaGold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { value / 10f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = FiestaOrange,
            trackColor = Color.DarkGray
        )
    }
}
