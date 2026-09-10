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
    aimAngle: Float,
    currentPower: Float,
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
                    onDragStart = {},
                    onDrag = { change, _ ->
                        val cueBall = balls.find { it.number == 0 }
                        if (cueBall != null) {
                            val newAngle = atan2(
                                change.position.y - cueBall.position.y,
                                change.position.x - cueBall.position.x
                            )
                            onAimChanged(newAngle)
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

            // --- 3D ISOMETRIC / PERSPECTIVE TILT TRANSFORMATION ---
            // Apply slight vertical perspective skew to give a 3D angled camera look
            drawContext.canvas.nativeCanvas.skew(0.0f, -0.08f)

            // 1. 3D Wooden Table Cabinet & Outer Frame with Depth
            drawRoundRect(
                color = Color(0xFF26140E), // Dark wood shadow base
                topLeft = Offset(-26f, -22f),
                size = Size(tableWidth + 52f, tableHeight + 60f),
                cornerRadius = CornerRadius(34f, 34f)
            )
            drawRoundRect(
                color = Color(0xFF4E342E), // Polished Mahogany Wood Rails
                topLeft = Offset(-22f, -22f),
                size = Size(tableWidth + 44f, tableHeight + 44f),
                cornerRadius = CornerRadius(30f, 30f)
            )

            // Inner rail cushion border
            drawRoundRect(
                color = Color(0xFF1B100D),
                topLeft = Offset(-10f, -10f),
                size = Size(tableWidth + 20f, tableHeight + 20f),
                cornerRadius = CornerRadius(18f, 18f)
            )

            // 2. Green Felt Surface with Subtle Gradient / Lighting
            drawRoundRect(
                color = FiestaGreenFelt,
                topLeft = Offset(0f, 0f),
                size = Size(tableWidth, tableHeight),
                cornerRadius = CornerRadius(8f, 8f)
            )

            // 3. Pockets (Realistic 3D recessed holes with brass collars)
            for (pocket in pockets) {
                // Brass collar
                drawCircle(
                    color = Color(0xFFD4AF37),
                    radius = pocket.radius + 4f,
                    center = Offset(pocket.position.x, pocket.position.y)
                )
                // Pocket shadow depth
                drawCircle(
                    color = Color(0xFF050505),
                    radius = pocket.radius,
                    center = Offset(pocket.position.x, pocket.position.y)
                )
            }

            // 4. Aiming Guide & 3D Elevated Cue Stick
            val cueBall = balls.find { it.number == 0 }
            if (!isSimulating && cueBall != null && !cueBall.isPocketed) {
                val dirX = cos(aimAngle)
                val dirY = sin(aimAngle)

                // Trajectory guide line
                val endX = cueBall.position.x + dirX * 350f
                val endY = cueBall.position.y + dirY * 350f

                drawLine(
                    color = Color.White.copy(alpha = 0.9f),
                    start = Offset(cueBall.position.x, cueBall.position.y),
                    end = Offset(endX, endY),
                    strokeWidth = 4f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                )

                // 3D Elevated Wooden Cue Stick
                val pullBack = 45f + (currentPower * 130f)
                val stickStartX = cueBall.position.x - dirX * pullBack
                val stickStartY = cueBall.position.y - dirY * pullBack
                val stickEndX = cueBall.position.x - dirX * 300f
                val stickEndY = cueBall.position.y - dirY * 300f

                // Cue Shaft with 3D shadow offset
                drawLine(
                    color = Color.Black.copy(alpha = 0.5f),
                    start = Offset(stickStartX + 3f, stickStartY + 6f),
                    end = Offset(stickEndX + 3f, stickEndY + 6f),
                    strokeWidth = 11f
                )
                drawLine(
                    color = Color(0xFFD7CCC8),
                    start = Offset(stickStartX, stickStartY),
                    end = Offset(stickEndX, stickEndY),
                    strokeWidth = 10f
                )
                // Cue Tip (Blue chalk point)
                drawCircle(
                    color = Color(0xFF29B6F6),
                    radius = 5.5f,
                    center = Offset(stickStartX, stickStartY)
                )
            }

            // 5. Balls with 3D Height Shadows & Glossy Spheres
            for (ball in balls) {
                if (ball.isPocketed) continue
                draw3DBall(ball)
            }

            drawContext.canvas.nativeCanvas.restore()
        }
    }
}

private fun DrawScope.draw3DBall(ball: Ball) {
    val center = Offset(ball.position.x, ball.position.y)

    // Realistic 3D Ball Drop Shadow (offset downward for 3D perspective depth)
    drawCircle(
        color = Color.Black.copy(alpha = 0.5f),
        radius = ball.radius * 1.05f,
        center = center + Offset(4f, 8f)
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

    // 3D Glossy Specular Highlight (giving it a shiny billiard ball look)
    drawCircle(
        color = Color.White.copy(alpha = 0.75f),
        radius = ball.radius * 0.3f,
        center = center + Offset(-ball.radius * 0.3f, -ball.radius * 0.3f)
    )
}
