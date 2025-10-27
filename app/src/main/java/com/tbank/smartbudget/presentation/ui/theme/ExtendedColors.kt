package com.tbank.smartbudget.presentation.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Расширенный класс цветовой схемы для приложения.
 * Сюда можно добавлять любые кастомные цвета,
 * которые не входят в стандартную ColorScheme Material 3.
 *
 * @property lightGrey Цвет, используемый для фона поля поиска.
 * @property customSuccess Цвет, используемый для кастомных индикаторов успеха (пример).
 */
@Immutable
data class ExtendedColors(
    // Кастомные цвета
    val lightGrey: Color,
    val customSuccess: Color,

    // Дополнительные цвета могут быть добавлены здесь
    // val chartBlue: Color,
    // val headerGradientEnd: Color
)

/**
 * Создает набор ExtendedColors для темного режима.
 *
 * @param colorScheme Стандартная ColorScheme, для удобства использования.
 */
fun darkExtendedColors(colorScheme: ColorScheme): ExtendedColors = ExtendedColors(
    lightGrey = colorScheme.surfaceVariant, // В темной теме фон поиска может быть как у карточек
    customSuccess = Color(0xFF66BB6A) // Пример зеленого
)

/**
 * Создает набор ExtendedColors для светлого режима.
 *
 * @param colorScheme Стандартная ColorScheme, для удобства использования.
 */
fun lightExtendedColors(colorScheme: ColorScheme): ExtendedColors = ExtendedColors(
    lightGrey = Color(0xFFEAEAEA), // Используем ваш кастомный светло-серый цвет
    customSuccess = Color(0xFF4CAF50) // Пример зеленого
)


/**
 * CompositionLocal для доступа к ExtendedColors из любой @Composable функции.
 * Используйте: `MaterialTheme.extendedColors`
 */
val LocalExtendedColors = staticCompositionLocalOf {
    lightExtendedColors(ColorScheme(
        primary = Color.Unspecified,
        onPrimary = Color.Unspecified,
        primaryContainer = Color.Unspecified,
        onPrimaryContainer = Color.Unspecified,
        secondary = Color.Unspecified,
        onSecondary = Color.Unspecified,
        secondaryContainer = Color.Unspecified,
        onSecondaryContainer = Color.Unspecified,
        tertiary = Color.Unspecified,
        onTertiary = Color.Unspecified,
        tertiaryContainer = Color.Unspecified,
        onTertiaryContainer = Color.Unspecified,
        error = Color.Unspecified,
        onError = Color.Unspecified,
        errorContainer = Color.Unspecified,
        onErrorContainer = Color.Unspecified,
        background = Color.Unspecified,
        onBackground = Color.Unspecified,
        surface = Color.Unspecified,
        onSurface = Color.Unspecified,
        surfaceVariant = Color.Unspecified,
        onSurfaceVariant = Color.Unspecified,
        inverseSurface = Color.Unspecified,
        inverseOnSurface = Color.Unspecified,
        outline = Color.Unspecified,
        outlineVariant = Color.Unspecified,
        scrim = Color.Unspecified,
        surfaceBright = Color.Unspecified,
        surfaceContainer = Color.Unspecified,
        surfaceContainerHigh = Color.Unspecified,
        surfaceContainerHighest = Color.Unspecified,
        surfaceContainerLow = Color.Unspecified,
        surfaceContainerLowest = Color.Unspecified,
        surfaceDim = Color.Unspecified,
        inversePrimary = Color.Unspecified,
        surfaceTint = TODO(),
    ))
}

/**
 * Удобное свойство для доступа к расширенным цветам.
 */
val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    get() = LocalExtendedColors.current
