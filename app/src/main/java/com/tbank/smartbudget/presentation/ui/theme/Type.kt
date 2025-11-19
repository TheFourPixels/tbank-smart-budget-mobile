package com.tbank.smartbudget.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tbank.smartbudget.R // Важно: R.font должен быть доступен

// Определение семейства шрифтов Roboto
private val RobotoFontFamily = FontFamily(
    Font(R.font.roboto, FontWeight.Normal), // 400
    Font(R.font.roboto_medium, FontWeight.Medium),   // 500
    Font(R.font.roboto_bold, FontWeight.Bold)       // 700
)

// Определение стилей типографики
val Typography = Typography(
    // Body Large (основной текст)
    bodyLarge = TextStyle(
        fontFamily = RobotoFontFamily,
        fontWeight = FontWeight.Normal, // 400 Regular
        fontSize = 16.sp,
        lineHeight = 16.sp, // Установка межстрочного интервала
        letterSpacing = 0.sp
    ),

    titleLarge = TextStyle(
        fontFamily = RobotoFontFamily,
        fontWeight = FontWeight.Bold, // Bold для заголовков
        fontSize = 22.sp,
        lineHeight = 23.sp,
        letterSpacing = 0.2.sp
    ),

    titleMedium = TextStyle(
        fontFamily = RobotoFontFamily,
        fontWeight = FontWeight.Medium, // Medium для подзаголовков
        fontSize = 19.sp,
        lineHeight = 23.sp,
        letterSpacing = 0.2.sp
    ),

    // Остальные стили по умолчанию также будут использовать Roboto:
    bodyMedium = TextStyle(fontFamily = RobotoFontFamily),
    bodySmall = TextStyle(fontFamily = RobotoFontFamily),
    labelLarge = TextStyle(fontFamily = RobotoFontFamily),
    labelMedium = TextStyle(fontFamily = RobotoFontFamily),
    labelSmall = TextStyle(fontFamily = RobotoFontFamily),
    headlineLarge = TextStyle(fontFamily = RobotoFontFamily),
    headlineMedium = TextStyle(fontFamily = RobotoFontFamily),
    headlineSmall = TextStyle(fontFamily = RobotoFontFamily),
    displayLarge = TextStyle(fontFamily = RobotoFontFamily),
    displayMedium = TextStyle(fontFamily = RobotoFontFamily),
    displaySmall = TextStyle(fontFamily = RobotoFontFamily),
)
