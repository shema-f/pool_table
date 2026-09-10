package com.example.fiestapooltable.ui.settings

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

@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    var soundEffects by remember { mutableStateOf(true) }
    var backgroundMusic by remember { mutableStateOf(true) }
    var aimingGuide by remember { mutableStateOf(true) }
    var vibration by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FiestaDarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
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
                    text = "⚙️ SETTINGS",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = FiestaGold
                )
                Spacer(modifier = Modifier.width(60.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Audio Settings Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(text = "AUDIO & SOUNDS", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FiestaOrange)
                    Spacer(modifier = Modifier.height(12.dp))
                    SettingSwitchRow(title = "Sound Effects", subtitle = "Cue strike, collisions, pocket drops", checked = soundEffects) { soundEffects = it }
                    Spacer(modifier = Modifier.height(8.dp))
                    SettingSwitchRow(title = "Background Music", subtitle = "Fiesta celebration soundtrack", checked = backgroundMusic) { backgroundMusic = it }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Gameplay Settings Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(text = "GAMEPLAY & CONTROLS", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FiestaOrange)
                    Spacer(modifier = Modifier.height(12.dp))
                    SettingSwitchRow(title = "Aiming Guide", subtitle = "Show prediction trajectory line", checked = aimingGuide) { aimingGuide = it }
                    Spacer(modifier = Modifier.height(8.dp))
                    SettingSwitchRow(title = "Haptic Vibration", subtitle = "Vibrate on cue impact", checked = vibration) { vibration = it }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // About Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "FIESTA POOL TABLE 🎱", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = FiestaGold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Version 1.0.0 (Production Release)", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Play • Friends • Competition • Championships", fontSize = 12.sp, color = FiestaOrange)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingSwitchRow(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = FiestaOrange)
        )
    }
}
