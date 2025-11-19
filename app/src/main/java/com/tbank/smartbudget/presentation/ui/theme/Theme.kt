package com.tbank.smartbudget.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

// --- 1. СТАНДАРТНЫЕ ЦВЕТОВЫЕ СХЕМЫ MATERIAL 3 ---

// Темная цветовая схема, использует цвета, определенные в Color.kt
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = DarkOnPrimary,
    secondary = SecondaryDark,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = Color(0xFF333333), // Цвет фона для карточек
    error = ErrorRed,
    scrim = Color.Black.copy(alpha = 0.8f) // Используется для фона модальных окон
)

// Светлая цветовая схема, использует цвета, определенные в Color.kt
private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = LightOnPrimary,
    secondary = SecondaryLight,
    background = LightBackground,
    surface = LightSurface,
    onBackground = LightOnSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant, // Используем для фона карточек/элементов
    error = ErrorRedLight,
    scrim = Color.Black.copy(alpha = 0.6f),
)

// --- 2. РАСШИРЕНИЕ ЦВЕТОВЫХ СХЕМ ---

/**
 * Основная тема приложения SmartBudget.
 * Внедряет MaterialTheme и ExtendedColors.
 */
@Composable
fun SmartBudgetTheme(
    // Определяем, какой режим установлен в системе
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Определяем расширенную цветовую схему на основе стандартной
    val extendedColors =  when {
        darkTheme -> darkExtendedColors(colorScheme)
        else -> lightExtendedColors(colorScheme)
    }

    // CompositionLocalProvider предоставляет кастомную схему всем Composable-функциям
    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography, // Используем шрифт, определенный в Typography.kt
            content = content
        )
    }
}