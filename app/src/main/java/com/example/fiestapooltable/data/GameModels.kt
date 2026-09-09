package com.example.fiestapooltable

data class User(
    val userId: String,
    val username: String,
    val avatarUrl: String,
    val level: Int,
    val xp: Long,
    val coins: Long,
    val trophies: Int
)

data class PlayerStats(
    val userId: String,
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val winRate: Float,
    val tournamentsPlayed: Int,
    val tournamentsWon: Int,
    // Play style stats (0f to 10f)
    val aggression: Float,
    val defense: Float,
    val trickShots: Float,
    val risk: Float
)

enum class GameMode {
    PRACTICE, AI_MATCH, LOCAL_FRIEND, ONLINE_PRIVATE, ONLINE_QUICK, CHAMPIONSHIP
}

enum class MatchStatus {
    PENDING, ACTIVE, COMPLETED, CANCELLED
}

data class Match(
    val matchId: String,
    val mode: GameMode,
    val player1Id: String,
    val player2Id: String,
    val status: MatchStatus,
    val winnerId: String?,
    val roomCode: String?
)

enum class TournamentFormat {
    SINGLE_ELIMINATION, ROUND_ROBIN
}

data class Tournament(
    val tournamentId: String,
    val name: String,
    val creatorId: String,
    val maxPlayers: Int,
    val format: TournamentFormat,
    val prizeDescription: String,
    val startTime: Long,
    val tournamentCode: String,
    val status: MatchStatus
)

data class TournamentPlayer(
    val tournamentId: String,
    val userId: String,
    val seed: Int
)

data class TournamentMatch(
    val tournamentMatchId: String,
    val tournamentId: String,
    val round: Int,
    val matchId: String?,
    val player1Id: String?,
    val player2Id: String?,
    val winnerId: String?
)

data class LeaderboardEntry(
    val rank: Int,
    val userId: String,
    val username: String,
    val avatarUrl: String,
    val score: Int,
    val ratingType: String
)

data class Achievement(
    val achievementId: String,
    val title: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean,
    val progress: Float
)

data class Reward(
    val rewardId: String,
    val title: String,
    val type: String,
    val amount: Long
)

data class Club(
    val clubId: String,
    val name: String,
    val description: String,
    val memberCount: Int,
    val ownerId: String
)
