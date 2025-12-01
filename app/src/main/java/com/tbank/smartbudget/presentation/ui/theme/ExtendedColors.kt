package com.tbank.smartbudget.presentation.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Расширенный класс цветовой схемы для приложения.
 * Сюда можно добавлять любые кастомные цвета,
 * которые не входят в стандартную ColorScheme Material 3.
 *
 * @property lightGray Цвет, используемый для фона поля поиска.
 */
@Immutable
data class ExtendedColors(
    // Кастомные цвета
    val lightGray: Color,
    val gradientGreen: Color,
    val gradientDarkBlue: Color
)
/**
 * Создает набор ExtendedColors для темного режима.
 *
 * @param colorScheme Стандартная ColorScheme, для удобства использования.
 */
fun darkExtendedColors(colorScheme: ColorScheme): ExtendedColors = ExtendedColors(
    lightGray = colorScheme.surfaceVariant, // В темной теме фон поиска может быть как у карточек
    gradientGreen = Color(0xFF8EAF48),
    gradientDarkBlue = Color(0xFF363E4E)

)

/**
 * Создает набор ExtendedColors для светлого режима.
 *
 * @param colorScheme Стандартная ColorScheme, для удобства использования.
 */
fun lightExtendedColors(colorScheme: ColorScheme): ExtendedColors = ExtendedColors(
    lightGray = Color(0xFFEAEAEA), // Используем ваш кастомный светло-серый цвет
    gradientGreen = Color(0xFF8EAF48),
    gradientDarkBlue = Color(0xFF363E4E)
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        lightGray = Color(0xFFEAEAEA),
        gradientGreen = Color(0xFF8EAF48),
        gradientDarkBlue = Color(0xFF363E4E)
    )
}
object SmartBudgetTheme {
    val colors : ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}