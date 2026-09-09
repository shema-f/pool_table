package com.example.fiestapooltable.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.fiestapooltable.game.Ball
import com.example.fiestapooltable.game.Pocket
import com.example.fiestapooltable.ui.theme.FiestaGreenFelt
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BilliardsTableView(
    modifier: Modifier = Modifier,
    balls: List<Ball>,
    pockets: List<Pocket>,
    tableWidth: Float,
    tableHeight: Float,
    isSimulating: Boolean,
    aimAngle: Float, // Angle in radians pointing from cue ball to target direction
    currentPower: Float, // 0f to 1f
    onAimChanged: (Float) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(tableWidth / tableHeight)
            .background(Color.Transparent)
            .pointerInput(isSimulating) {
                if (isSimulating) return@pointerInput

                detectDragGestures(
                    onDragStart = { offset ->
                        // Optional start handle if needed
                    },
                    onDrag = { change, _ ->
                        val cueBall = balls.find { it.number == 0 }
                        if (cueBall != null) {
                            // Calculate angle from cue ball to touch drag position
                            // Note: We map touch coordinates to table physical coordinates
                            // For simplicity, drag delta rotates aiming angle
                            val dx = change.position.x - (tableWidth / 2f) // relative center check
                            val dy = change.position.y - (tableHeight / 2f)
                            if (dx != 0f || dy != 0f) {
                                val newAngle = atan2(
                                    change.position.y - cueBall.position.y,
                                    change.position.x - cueBall.position.x
                                )
                                onAimChanged(newAngle)
                            }
                        }
                    },
                    onDragEnd = {},
                    onDragCancel = {}
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val scaleX = size.width / tableWidth
            val scaleY = size.height / tableHeight
            val scale = minOf(scaleX, scaleY)

            val offsetX = (size.width - tableWidth * scale) / 2f
            val offsetY = (size.height - tableHeight * scale) / 2f

            drawContext.canvas.nativeCanvas.save()
            drawContext.canvas.nativeCanvas.translate(offsetX, offsetY)
            drawContext.canvas.nativeCanvas.scale(scale, scale)

            // 1. Wooden Outer Frame & Rails (Real pool table styling)
            drawRoundRect(
                color = Color(0xFF3E2723), // Deep Mahogany Wood
                topLeft = Offset(-22f, -22f),
                size = Size(tableWidth + 44f, tableHeight + 44f),
                cornerRadius = CornerRadius(30f, 30f)
            )

            // Inner rail cushion border
            drawRoundRect(
                color = Color(0xFF271916),
                topLeft = Offset(-10f, -10f),
                size = Size(tableWidth + 20f, tableHeight + 20f),
                cornerRadius = CornerRadius(18f, 18f)
            )

            // 2. Green Felt Surface
            drawRoundRect(
                color = FiestaGreenFelt,
                topLeft = Offset(0f, 0f),
                size = Size(tableWidth, tableHeight),
                cornerRadius = CornerRadius(8f, 8f)
            )

            // 3. Pockets (Realistic dark recessed holes with brass collars)
            for (pocket in pockets) {
                drawCircle(
                    color = Color(0xFFD4AF37),
                    radius = pocket.radius + 4f,
                    center = Offset(pocket.position.x, pocket.position.y)
                )
                drawCircle(
                    color = Color(0xFF0A0A0A),
                    radius = pocket.radius,
                    center = Offset(pocket.position.x, pocket.position.y)
                )
            }

            // 4. Aiming Guide & Visible Cue Stick
            val cueBall = balls.find { it.number == 0 }
            if (!isSimulating && cueBall != null && !cueBall.isPocketed) {
                // Aim direction vector
                val dirX = cos(aimAngle)
                val dirY = sin(aimAngle)

                // Trajectory guide line (forward from cue ball)
                val endX = cueBall.position.x + dirX * 350f
                val endY = cueBall.position.y + dirY * 350f

                drawLine(
                    color = Color.White.copy(alpha = 0.85f),
                    start = Offset(cueBall.position.x, cueBall.position.y),
                    end = Offset(endX, endY),
                    strokeWidth = 3.5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                )

                // Cue Stick (Always visible, positioned opposite to aimAngle, pulled back based on currentPower)
                val pullBack = 45f + (currentPower * 120f)
                val stickStartX = cueBall.position.x - dirX * pullBack
                val stickStartY = cueBall.position.y - dirY * pullBack
                val stickEndX = cueBall.position.x - dirX * 280f
                val stickEndY = cueBall.position.y - dirY * 280f

                // Cue Shaft (Wood Grain)
                drawLine(
                    color = Color(0xFFD7CCC8),
                    start = Offset(stickStartX, stickStartY),
                    end = Offset(stickEndX, stickEndY),
                    strokeWidth = 10f
                )
                // Cue Tip (Blue chalk point)
                drawCircle(
                    color = Color(0xFF29B6F6),
                    radius = 5f,
                    center = Offset(stickStartX, stickStartY)
                )
            }

            // 5. Balls
            for (ball in balls) {
                if (ball.isPocketed) continue
                drawBall(ball)
            }

            drawContext.canvas.nativeCanvas.restore()
        }
    }
}

private fun DrawScope.drawBall(ball: Ball) {
    val center = Offset(ball.position.x, ball.position.y)

    // Ball Shadow
    drawCircle(
        color = Color.Black.copy(alpha = 0.4f),
        radius = ball.radius,
        center = center + Offset(3f, 6f)
    )

    // Ball Base Color
    drawCircle(
        color = ball.color,
        radius = ball.radius,
        center = center
    )

    // Number circle for numbered balls
    if (ball.number > 0) {
        drawCircle(
            color = Color.White,
            radius = ball.radius * 0.45f,
            center = center
        )
    }

    // Specular highlight for 3D glossy pool ball look
    drawCircle(
        color = Color.White.copy(alpha = 0.7f),
        radius = ball.radius * 0.28f,
        center = center + Offset(-ball.radius * 0.3f, -ball.radius * 0.3f)
    )
}
