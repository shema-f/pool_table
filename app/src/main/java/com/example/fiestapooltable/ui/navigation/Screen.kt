package com.example.fiestapooltable.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Practice : Screen("practice")
    object AiMatch : Screen("ai_match")
    object LocalLobby : Screen("local_lobby")
    object OnlineLobby : Screen("online_lobby")
    object Tournaments : Screen("tournaments")
    object Profile : Screen("profile")
    object Leaderboard : Screen("leaderboard")
    object Settings : Screen("settings")
}
