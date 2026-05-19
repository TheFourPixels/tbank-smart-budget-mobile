package com.tbank.smartbudget.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

// --- 1. СТАНДАРТНЫЕ ЦВЕТОВЫЕ СХЕМЫ MATERIAL 3 ---

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = DarkOnPrimary,
    secondary = SecondaryDark,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    error = ErrorRed,
    scrim = Color.Black.copy(alpha = 0.8f)
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = LightOnPrimary,
    secondary = SecondaryLight,
    background = LightBackground,
    surface = LightSurface,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    error = ErrorRedLight,
    scrim = Color.Black.copy(alpha = 0.6f),
)

// --- 2. РАСШИРЕНИЕ ЦВЕТОВЫХ СХЕМ ---

@Composable
fun SmartBudgetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors =  when {
        darkTheme -> darkExtendedColors(colorScheme)
        else -> lightExtendedColors(colorScheme)
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
