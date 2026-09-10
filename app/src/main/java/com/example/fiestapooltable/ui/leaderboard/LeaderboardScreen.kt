package com.example.fiestapooltable.ui.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fiestapooltable.LeaderboardEntry
import com.example.fiestapooltable.ui.theme.FiestaDarkBackground
import com.example.fiestapooltable.ui.theme.FiestaGold
import com.example.fiestapooltable.ui.theme.FiestaOrange

@Composable
fun LeaderboardScreen(
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Global, 1: Regional, 2: Friends

    val entries = listOf(
        LeaderboardEntry(1, "u1", "Alex", "", 2450, "Global"),
        LeaderboardEntry(2, "u2", "John", "", 2310, "Global"),
        LeaderboardEntry(3, "u3", "Eric", "", 2190, "Global"),
        LeaderboardEntry(4, "u4", "David", "", 2050, "Global"),
        LeaderboardEntry(5, "u5", "Michael", "", 1980, "Global"),
        LeaderboardEntry(6, "u6", "Sarah", "", 1850, "Global"),
        LeaderboardEntry(7, "u7", "Carlos", "", 1720, "Global")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FiestaDarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
                    text = "📊 LEADERBOARD",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = FiestaGold
                )
                Spacer(modifier = Modifier.width(60.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = FiestaGold
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Global") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Regional") })
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Friends") })
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Leaderboard List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(entries) { entry ->
                    LeaderboardItem(entry = entry)
                }
            }
        }
    }
}

@Composable
fun LeaderboardItem(entry: LeaderboardEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (entry.rank <= 3) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "#${entry.rank}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (entry.rank == 1) FiestaGold else Color.White,
                    modifier = Modifier.width(40.dp)
                )

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(FiestaOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = entry.username.take(1),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = entry.username,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🏆", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${entry.score}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = FiestaGold
                )
            }
        }
    }
}
