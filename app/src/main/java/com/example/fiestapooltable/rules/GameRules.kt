package com.example.fiestapooltable.rules

import com.example.fiestapooltable.game.Ball

enum class BallGroup {
    UNASSIGNED, YELLOWS, REDS
}

data class GameRuleState(
    val currentPlayer: Int = 1, // 1 or 2
    val player1Group: BallGroup = BallGroup.UNASSIGNED,
    val player2Group: BallGroup = BallGroup.UNASSIGNED,
    val isGameOver: Boolean = false,
    val winner: Int? = null,
    val message: String = "Player 1's Turn: Break!"
)

object GameRulesEngine {

    fun evaluateShot(
        currentState: GameRuleState,
        pocketedBallsThisShot: List<Ball>,
        scratchOccurred: Boolean,
        firstHitNumber: Int?
    ): GameRuleState {
        if (currentState.isGameOver) return currentState

        var p1Group = currentState.player1Group
        var p2Group = currentState.player2Group
        var nextPlayer = currentState.currentPlayer
        var isGameOver = currentState.isGameOver
        var winner = currentState.winner
        val message: String

        // 1. Check Scratch (Cue ball sunk)
        if (scratchOccurred) {
            val playerStr = if (nextPlayer == 1) "Player 1" else "Player 2"
            nextPlayer = if (nextPlayer == 1) 2 else 1
            return currentState.copy(
                currentPlayer = nextPlayer,
                message = "Foul! $playerStr scratched. Ball in hand for Player $nextPlayer."
            )
        }

        // 2. Check if any balls were pocketed
        if (pocketedBallsThisShot.isEmpty()) {
            nextPlayer = if (nextPlayer == 1) 2 else 1
            return currentState.copy(
                currentPlayer = nextPlayer,
                message = "Miss! Turn switches to Player $nextPlayer."
            )
        }

        // 3. Process pocketed balls (Yellows vs Reds)
        var pocketed8Ball = false
        val currentPlayerGroup = if (nextPlayer == 1) p1Group else p2Group

        for (ball in pocketedBallsThisShot) {
            if (ball.number == 8) {
                pocketed8Ball = true
                break
            }

            if (currentPlayerGroup == BallGroup.UNASSIGNED && ball.number != 8) {
                val assigned = if (ball.isYellow) BallGroup.YELLOWS else BallGroup.REDS
                if (nextPlayer == 1) {
                    p1Group = assigned
                    p2Group = if (assigned == BallGroup.YELLOWS) BallGroup.REDS else BallGroup.YELLOWS
                } else {
                    p2Group = assigned
                    p1Group = if (assigned == BallGroup.YELLOWS) BallGroup.REDS else BallGroup.YELLOWS
                }
            }
        }

        if (pocketed8Ball) {
            isGameOver = true
            winner = nextPlayer
            message = "Player $nextPlayer pocketed the 8-Ball and wins the match!"
            return currentState.copy(
                isGameOver = isGameOver,
                winner = winner,
                message = message
            )
        }

        message = "Player $nextPlayer pocketed ball(s) and continues turn!"
        return currentState.copy(
            currentPlayer = nextPlayer,
            player1Group = p1Group,
            player2Group = p2Group,
            message = message
        )
    }
}
