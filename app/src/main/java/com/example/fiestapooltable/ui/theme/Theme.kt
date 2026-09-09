package com.example.fiestapooltable.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val FiestaDarkColorScheme = darkColorScheme(
    primary = FiestaOrange,
    secondary = FiestaGold,
    tertiary = FiestaGreenLight,
    background = FiestaDarkBackground,
    surface = FiestaSurfaceDark,
    onPrimary = FiestaWhite,
    onSecondary = Color.Black,
    onBackground = FiestaWhite,
    onSurface = FiestaWhite
)

private val FiestaLightColorScheme = lightColorScheme(
    primary = FiestaOrange,
    secondary = FiestaGold,
    tertiary = FiestaGreenFelt,
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onPrimary = FiestaWhite,
    onSecondary = Color.Black,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun FiestaPoolTableTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Default to false to preserve distinctive Fiesta branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> FiestaDarkColorScheme
        else -> FiestaLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
