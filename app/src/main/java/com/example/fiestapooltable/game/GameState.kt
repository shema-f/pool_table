package com.example.fiestapooltable.game

import androidx.compose.ui.graphics.Color
import com.example.fiestapooltable.physics.Vector2D

data class Ball(
    val id: Int,
    var position: Vector2D,
    var velocity: Vector2D = Vector2D(0f, 0f),
    val radius: Float = 16f,
    val color: Color,
    val number: Int, // 0 = cue ball, 1-7 = yellow, 8 = 8-ball, 9-15 = red
    val isYellow: Boolean = false,
    val isRed: Boolean = false,
    var isPocketed: Boolean = false
)

data class Pocket(
    val position: Vector2D,
    val radius: Float = 28f
)

data class TableConfig(
    val width: Float = 800f,
    val height: Float = 440f,
    val cushionWidth: Float = 32f
)

fun createInitialBalls(tableWidth: Float, tableHeight: Float): List<Ball> {
    val balls = mutableListOf<Ball>()
    val radius = 16f

    // Cue ball position (left side)
    balls.add(
        Ball(
            id = 0,
            position = Vector2D(tableWidth * 0.25f, tableHeight * 0.5f),
            color = Color.White,
            number = 0
        )
    )

    // Rack position (right side)
    val startX = tableWidth * 0.70f
    val startY = tableHeight * 0.5f
    val rowSpacing = radius * 1.732f // sqrt(3) * radius
    val ballSpacing = radius * 2f

    // UK 8-Ball Color Set: Yellows (1-7), 8-Ball (8), Reds (9-15)
    // Rack arrangement following standard triangle with 8-ball in center
    val rackOrder = listOf(1, 9, 8, 2, 3, 10, 11, 4, 12, 13, 5, 14, 15, 6, 7)
    var ballIndex = 0

    for (row in 0 until 5) {
        for (col in 0..row) {
            if (ballIndex >= rackOrder.size) break
            val number = rackOrder[ballIndex]
            val x = startX + row * rowSpacing
            val y = startY + (col - row / 2f) * ballSpacing

            val color: Color
            val isY: Boolean
            val isR: Boolean

            when {
                number == 8 -> {
                    color = Color.Black
                    isY = false
                    isR = false
                }
                number in 1..7 -> {
                    color = Color(0xFFFFD700) // Rich Yellow
                    isY = true
                    isR = false
                }
                else -> {
                    color = Color(0xFFD32F2F) // Rich Red
                    isY = false
                    isR = true
                }
            }

            balls.add(
                Ball(
                    id = ballIndex + 1,
                    position = Vector2D(x, y),
                    color = color,
                    number = number,
                    isYellow = isY,
                    isRed = isR
                )
            )
            ballIndex++
        }
    }

    return balls
}

fun createTablePockets(tableWidth: Float, tableHeight: Float): List<Pocket> {
    val offset = 12f
    return listOf(
        Pocket(Vector2D(offset, offset)), // Top-Left
        Pocket(Vector2D(tableWidth / 2f, offset - 4f)), // Top-Middle
        Pocket(Vector2D(tableWidth - offset, offset)), // Top-Right
        Pocket(Vector2D(offset, tableHeight - offset)), // Bottom-Left
        Pocket(Vector2D(tableWidth / 2f, tableHeight - offset + 4f)), // Bottom-Middle
        Pocket(Vector2D(tableWidth - offset, tableHeight - offset)) // Bottom-Right
    )
}
