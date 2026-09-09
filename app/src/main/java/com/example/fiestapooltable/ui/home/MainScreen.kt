package com.example.fiestapooltable.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fiestapooltable.ui.theme.FiestaGreenDark
import com.example.fiestapooltable.ui.theme.FiestaGreenFelt
import com.example.fiestapooltable.ui.theme.FiestaGreenLight
import com.example.fiestapooltable.ui.theme.FiestaGold
import com.example.fiestapooltable.ui.theme.FiestaOrange

@Composable
fun MainScreen(
    onNavigate: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(FiestaGreenDark, FiestaGreenFelt, MaterialTheme.colorScheme.background)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Title / Branding
            Text(
                text = "🎱",
                fontSize = 56.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "FIESTA POOL TABLE",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = FiestaGold,
                textAlign = TextAlign.Center,
                letterSpacing = 2.sp
            )
            Text(
                text = "Play • Friends • Competition • Championships",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Menu Buttons
            MenuButton(text = "🎮 PLAY VS AI", color = FiestaOrange) {
                onNavigate("ai_match")
            }
            Spacer(modifier = Modifier.height(12.dp))

            MenuButton(text = "🎯 PRACTICE", color = FiestaGreenLight) {
                onNavigate("practice")
            }
            Spacer(modifier = Modifier.height(12.dp))

            MenuButton(text = "🤝 PLAY WITH FRIEND (LOCAL)", color = MaterialTheme.colorScheme.primary) {
                onNavigate("local_lobby")
            }
            Spacer(modifier = Modifier.height(12.dp))

            MenuButton(text = "🌐 ONLINE MATCH", color = MaterialTheme.colorScheme.secondary) {
                onNavigate("online_lobby")
            }
            Spacer(modifier = Modifier.height(12.dp))

            MenuButton(text = "🏆 CHAMPIONSHIPS", color = FiestaGold) {
                onNavigate("tournaments")
            }
            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(24.dp))

            // Secondary Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(text = "👤 Profile", modifier = Modifier.weight(1f)) {
                    onNavigate("profile")
                }
                SecondaryButton(text = "📊 Rankings", modifier = Modifier.weight(1f)) {
                    onNavigate("leaderboard")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(text = "⚙️ Settings", modifier = Modifier.fillMaxWidth()) {
                onNavigate("settings")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun MenuButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun SecondaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.5.dp, FiestaGold),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = FiestaGold)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
