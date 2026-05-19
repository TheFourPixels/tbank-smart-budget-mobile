package com.tbank.smartbudget.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Расширенный класс цветовой схемы для приложения.
 * Сюда можно добавлять любые кастомные цвета,
 * которые не входят в стандартную ColorScheme Material 3.
 */
@Immutable
data class ExtendedColors(
    val lightGray: Color,
    val gradientGreen: Color,
    val gradientDarkBlue: Color,
    val blue: Color,
    val yellow: Color,
    val gradientBlue: Color,
    val gradientYellow: Color,
    val gradientViolet: Color,
    val gradientDarkViolet: Color,
    val cardBackground: Color,
    val shadowColor: Color
)

/**
 * Создает набор ExtendedColors для темного режима.
 */
fun darkExtendedColors(colorScheme: ColorScheme): ExtendedColors = ExtendedColors(
    lightGray = Color(0xFF333333),
    gradientGreen = Color(0xFF8EAF48),
    gradientDarkBlue = Color(0xFF363E4E),
    blue = Color(0xFF5589F1),
    yellow = Color(0xFFFFDD2D),
    gradientBlue = Color(0xFF21326D),
    gradientYellow = Color(0xFFF6D583),
    gradientViolet = Color(0xFF554E95),
    gradientDarkViolet = Color(0xFF21326D),
    cardBackground = Color(0xFF1E1E1E),
    shadowColor = Color.Black.copy(alpha = 0.5f)
)

/**
 * Создает набор ExtendedColors для светлого режима.
 */
fun lightExtendedColors(colorScheme: ColorScheme): ExtendedColors = ExtendedColors(
    lightGray = Color(0xFFEAEAEA),
    gradientGreen = Color(0xFF8EAF48),
    gradientDarkBlue = Color(0xFF363E4E),
    blue = Color(0xFF5589F1),
    yellow = Color(0xFFFFDD2D),
    gradientBlue = Color(0xFF21326D),
    gradientYellow = Color(0xFFF6D583),
    gradientViolet = Color(0xFF554E95),
    gradientDarkViolet = Color(0xFF21326D),
    cardBackground = Color.White,
    shadowColor = Color.Black.copy(alpha = 0.15f)
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        lightGray = Color(0xFFEAEAEA),
        gradientGreen = Color(0xFF8EAF48),
        gradientDarkBlue = Color(0xFF363E4E),
        blue = Color(0xFF5589F1),
        yellow = Color(0xFFFFDD2D),
        gradientBlue = Color(0xFF21326D),
        gradientYellow = Color(0xFFF6D583),
        gradientViolet = Color(0xFF554E95),
        gradientDarkViolet = Color(0xFF21326D),
        cardBackground = Color.White,
        shadowColor = Color.Black.copy(alpha = 0.15f)
    )
}

object SmartBudgetTheme {
    val colors : ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}
