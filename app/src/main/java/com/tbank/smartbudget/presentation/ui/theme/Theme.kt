package com.tbank.smartbudget.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography as MaterialTypography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Темная цветовая схема, использует цвета, определенные в Color.kt
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = DarkOnPrimary,
    secondary = SecondaryDark,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = Color(0xFF333333) // Цвет фона для карточек
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
)

/**
 * Основная тема приложения SmartBudget.
 * * Автоматически переключается между LightColorScheme и DarkColorScheme
 * в зависимости от настроек системы.
 */
@Composable
fun SmartBudgetTheme(
    // Определяем, какой режим установлен в системе
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Если в системе установлена темная тема, используем DarkColorScheme
        darkTheme -> DarkColorScheme
        // Иначе (если светлая тема) используем LightColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

/**
 * Заглушка для Типографики.
 * Можно расширить для настройки шрифтов, размеров и стилей текста.
 */
val AppTypography = MaterialTypography()
