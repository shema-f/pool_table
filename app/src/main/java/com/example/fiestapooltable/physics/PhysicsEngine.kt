package com.example.fiestapooltable.physics

import com.example.fiestapooltable.game.Ball
import com.example.fiestapooltable.game.Pocket
import kotlin.math.sqrt

object PhysicsEngine {
    private const val FRICTION = 0.985f // Surface rolling friction / damping per frame
    private const val STOP_VELOCITY_THRESHOLD = 0.05f
    private const val RESTITUTION = 0.8f // Bounciness of cushions and balls

    fun updatePhysics(
        balls: MutableList<Ball>,
        pockets: List<Pocket>,
        tableWidth: Float,
        tableHeight: Float,
        cushionBounds: Float = 24f
    ): Boolean {
        var anyMoving = false

        for (ball in balls) {
            if (ball.isPocketed) continue

            // 1. Apply velocity
            if (ball.velocity.magnitude() > STOP_VELOCITY_THRESHOLD) {
                ball.position += ball.velocity
                ball.velocity *= FRICTION
                anyMoving = true
            } else {
                ball.velocity = Vector2D(0f, 0f)
            }

            // 2. Check pocket collisions
            for (pocket in pockets) {
                val dist = ball.position.distanceTo(pocket.position)
                if (dist < pocket.radius) {
                    ball.isPocketed = true
                    ball.velocity = Vector2D(0f, 0f)
                    // If cue ball (number 0), respawn in center for testing
                    if (ball.number == 0) {
                        ball.isPocketed = false
                        ball.position = Vector2D(tableWidth * 0.25f, tableHeight * 0.5f)
                    }
                }
            }

            // 3. Check cushion collisions
            val minX = cushionBounds + ball.radius
            val maxX = tableWidth - cushionBounds - ball.radius
            val minY = cushionBounds + ball.radius
            val maxY = tableHeight - cushionBounds - ball.radius

            if (ball.position.x < minX) {
                ball.position.x = minX
                ball.velocity.x = -ball.velocity.x * RESTITUTION
            } else if (ball.position.x > maxX) {
                ball.position.x = maxX
                ball.velocity.x = -ball.velocity.x * RESTITUTION
            }

            if (ball.position.y < minY) {
                ball.position.y = minY
                ball.velocity.y = -ball.velocity.y * RESTITUTION
            } else if (ball.position.y > maxY) {
                ball.position.y = maxY
                ball.velocity.y = -ball.velocity.y * RESTITUTION
            }
        }

        // 4. Check ball-to-ball collisions
        for (i in balls.indices) {
            for (j in i + 1 until balls.size) {
                val b1 = balls[i]
                val b2 = balls[j]
                if (b1.isPocketed || b2.isPocketed) continue

                val dist = b1.position.distanceTo(b2.position)
                val minDist = b1.radius + b2.radius

                if (dist < minDist) {
                    anyMoving = true
                    // Resolve overlap
                    val overlap = 0.5f * (minDist - dist)
                    val dir = (b2.position - b1.position).normalized()
                    b1.position -= dir * overlap
                    b2.position += dir * overlap

                    // Elastic collision physics
                    val normal = (b2.position - b1.position).normalized()
                    val relVel = b2.velocity - b1.velocity
                    val velAlongNormal = relVel.dot(normal)

                    if (velAlongNormal < 0) {
                        val impulse = normal * (-(1 + RESTITUTION) * velAlongNormal / 2f)
                        b1.velocity -= impulse
                        b2.velocity += impulse
                    }
                }
            }
        }

        return anyMoving
    }
}
