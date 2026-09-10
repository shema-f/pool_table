package com.example.fiestapooltable.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fiestapooltable.game.*
import com.example.fiestapooltable.physics.PhysicsEngine
import com.example.fiestapooltable.physics.Vector2D
import com.example.fiestapooltable.rules.GameRuleState
import com.example.fiestapooltable.rules.GameRulesEngine
import com.example.fiestapooltable.ui.theme.FiestaDarkBackground
import com.example.fiestapooltable.ui.theme.FiestaGold
import com.example.fiestapooltable.ui.theme.FiestaOrange
import kotlinx.coroutines.delay
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GameScreen(
    gameModeTitle: String,
    isAiMatch: Boolean,
    onBack: () -> Unit
) {
    val tableWidth = 800f
    val tableHeight = 440f
    val balls = remember { mutableStateListOf(*createInitialBalls(tableWidth, tableHeight).toTypedArray()) }
    val pockets = remember { createTablePockets(tableWidth, tableHeight) }

    var isSimulating by remember { mutableStateOf(false) }
    var ruleState by remember { mutableStateOf(GameRuleState()) }
    val previouslyPocketedIds = remember { mutableSetOf<Int>() }
    
    // 3D vs Table Mode toggle
    var is3DMode by remember { mutableStateOf(false) }

    // Aiming and Power state
    var aimAngle by remember { mutableStateOf(0f) }
    var currentPower by remember { mutableStateOf(0.3f) }
    var aiThinking by remember { mutableStateOf(false) }

    // AI turn logic
    LaunchedEffect(ruleState.currentPlayer, isSimulating, isAiMatch) {
        if (isAiMatch && ruleState.currentPlayer == 2 && !isSimulating && !ruleState.isGameOver && !aiThinking) {
            aiThinking = true
            ruleState = ruleState.copy(message = "🤖 AI is thinking and aiming...")
            delay(1200L)

            val cueBall = balls.find { it.number == 0 }
            val targetBall = balls.filter { !it.isPocketed && it.number != 0 }.randomOrNull()

            if (cueBall != null && targetBall != null) {
                val dx = targetBall.position.x - cueBall.position.x
                val dy = targetBall.position.y - cueBall.position.y
                val angle = atan2(dy, dx) + (Math.random() * 0.1 - 0.05).toFloat()
                val power = (15f + Math.random() * 15f).toFloat()

                val velocityX = cos(angle) * power
                val velocityY = sin(angle) * power

                cueBall.velocity = Vector2D(velocityX, velocityY)
                isSimulating = true
            }
            aiThinking = false
        }
    }

    // Physics simulation loop & Rule Evaluation on stop
    LaunchedEffect(isSimulating) {
        if (isSimulating) {
            while (true) {
                withFrameNanos { _ ->
                    val moving = PhysicsEngine.updatePhysics(balls, pockets, tableWidth, tableHeight)
                    if (!moving) {
                        isSimulating = false

                        val newlyPocketed = balls.filter { it.isPocketed && it.id !in previouslyPocketedIds }
                        val scratch = newlyPocketed.any { it.number == 0 }
                        
                        for (b in newlyPocketed) {
                            previouslyPocketedIds.add(b.id)
                        }

                        ruleState = GameRulesEngine.evaluateShot(
                            currentState = ruleState,
                            pocketedBallsThisShot = newlyPocketed.filter { it.number != 0 },
                            scratchOccurred = scratch,
                            firstHitNumber = null
                        )
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FiestaDarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(onClick = onBack) {
                    Text("← Exit")
                }
                Text(
                    text = gameModeTitle.uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = FiestaGold
                )
                Row {
                    OutlinedButton(onClick = { is3DMode = !is3DMode }) {
                        Text(if (is3DMode) "2D Table" else "🧊 3D GLB")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = {
                            balls.clear()
                            balls.addAll(createInitialBalls(tableWidth, tableHeight))
                            previouslyPocketedIds.clear()
                            ruleState = GameRuleState()
                            isSimulating = false
                        }
                    ) {
                        Text("Reset")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Player Turn / Group Status Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                PlayerStatusCard(
                    playerName = if (isAiMatch) "Player (Yellows)" else "Player 1",
                    group = ruleState.player1Group.name,
                    isActive = ruleState.currentPlayer == 1
                )
                PlayerStatusCard(
                    playerName = if (isAiMatch) "AI Opponent (Reds)" else "Player 2",
                    group = ruleState.player2Group.name,
                    isActive = ruleState.currentPlayer == 2
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Display: 3D SceneView GLB Model OR 2D/Isometric Physics Table
            if (is3DMode) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Billiards3DView()
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        BilliardsTableView(
                            balls = balls,
                            pockets = pockets,
                            tableWidth = tableWidth,
                            tableHeight = tableHeight,
                            isSimulating = isSimulating || ruleState.isGameOver || (isAiMatch && ruleState.currentPlayer == 2),
                            aimAngle = aimAngle,
                            currentPower = currentPower,
                            onAimChanged = { newAngle -> aimAngle = newAngle }
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Side Energy / Power Column Meter & Shoot Button
                    Column(
                        modifier = Modifier
                            .width(60.dp)
                            .fillMaxHeight(0.9f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "POWER", fontSize = 10.sp, color = FiestaGold, fontWeight = FontWeight.Bold)

                        Box(
                            modifier = Modifier
                                .width(32.dp)
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF1E1E1E))
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDrag = { change, dragAmount ->
                                            val delta = -dragAmount.y * 0.005f
                                            currentPower = (currentPower + delta).coerceIn(0.05f, 1.0f)
                                            change.consume()
                                        }
                                    )
                                },
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .fillMaxHeight(currentPower)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (currentPower > 0.7f) Color.Red
                                        else if (currentPower > 0.4f) FiestaOrange
                                        else FiestaGold
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                val cueBall = balls.find { it.number == 0 }
                                if (cueBall != null && !isSimulating && !ruleState.isGameOver && !(isAiMatch && ruleState.currentPlayer == 2)) {
                                    val speed = currentPower * 38f
                                    val velocityX = cos(aimAngle) * speed
                                    val velocityY = sin(aimAngle) * speed
                                    cueBall.velocity = Vector2D(velocityX, velocityY)
                                    isSimulating = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FiestaOrange),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "⚡\nSHOOT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                lineHeight = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Rule Message Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = ruleState.message,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (ruleState.isGameOver) FiestaGold else FiestaOrange
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun PlayerStatusCard(playerName: String, group: String, isActive: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = playerName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = "Group: $group", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
