package com.example.fiestapooltable.physics

import kotlin.math.sqrt

data class Vector2D(var x: Float, var y: Float) {
    operator fun plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)
    operator fun minus(other: Vector2D) = Vector2D(x - other.x, y - other.y)
    operator fun times(scalar: Float) = Vector2D(x * scalar, y * scalar)
    operator fun div(scalar: Float) = Vector2D(x / scalar, y / scalar)

    fun magnitude(): Float = sqrt(x * x + y * y)

    fun normalized(): Vector2D {
        val mag = magnitude()
        return if (mag > 0f) this / mag else Vector2D(0f, 0f)
    }

    fun distanceTo(other: Vector2D): Float = (this - other).magnitude()

    fun dot(other: Vector2D): Float = x * other.x + y * other.y
}
