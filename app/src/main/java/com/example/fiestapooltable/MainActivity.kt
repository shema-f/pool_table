package com.example.fiestapooltable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fiestapooltable.ui.game.GameScreen
import com.example.fiestapooltable.ui.home.MainScreen
import com.example.fiestapooltable.ui.leaderboard.LeaderboardScreen
import com.example.fiestapooltable.ui.lobby.LocalLobbyScreen
import com.example.fiestapooltable.ui.lobby.OnlineLobbyScreen
import com.example.fiestapooltable.ui.profile.ProfileScreen
import com.example.fiestapooltable.ui.settings.SettingsScreen
import com.example.fiestapooltable.ui.tournaments.TournamentScreen
import com.example.fiestapooltable.ui.theme.FiestaPoolTableTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FiestaPoolTableTheme {
                var currentRoute by rememberSaveable { mutableStateOf("home") }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentRoute) {
                            "home" -> MainScreen(onNavigate = { route -> currentRoute = route })
                            "practice" -> GameScreen(gameModeTitle = "Practice Mode", isAiMatch = false, onBack = { currentRoute = "home" })
                            "ai_match" -> GameScreen(gameModeTitle = "Match vs AI", isAiMatch = true, onBack = { currentRoute = "home" })
                            "local_lobby" -> LocalLobbyScreen(onStartMatch = { currentRoute = "practice" }, onBack = { currentRoute = "home" })
                            "online_lobby" -> OnlineLobbyScreen(onStartMatch = { currentRoute = "practice" }, onBack = { currentRoute = "home" })
                            "tournaments" -> TournamentScreen(onBack = { currentRoute = "home" })
                            "profile" -> ProfileScreen(onBack = { currentRoute = "home" })
                            "leaderboard" -> LeaderboardScreen(onBack = { currentRoute = "home" })
                            "settings" -> SettingsScreen(onBack = { currentRoute = "home" })
                            else -> PlaceholderScreen(route = currentRoute, onBack = { currentRoute = "home" })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(route: String, onBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Screen: ${route.uppercase()}", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Coming up in upcoming phases!", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onBack) {
                Text(text = "Back to Main Menu")
            }
        }
    }
}
