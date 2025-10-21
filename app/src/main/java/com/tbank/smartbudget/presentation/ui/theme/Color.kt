package com.tbank.smartbudget.presentation.ui.theme

import androidx.compose.ui.graphics.Color

// --- ТЕМНЫЕ ЦВЕТА (DarkColorScheme) ---
val PrimaryDark = Color(0xFF8EAF48) // Яркий зеленый (Акцентный)
val SecondaryDark = Color(0xFF363E4E) // Глубокий зеленый (Градиент)
val TertiaryDark = Color(0xFFB00020) // Красный (Ошибка/Траты)

val DarkBackground = Color(0xFF121212) // Темный фон
val DarkSurface = Color(0xFF1E1E1E) // Темная поверхность карточек
val DarkOnSurface = Color(0xFFE0E0E0) // Светлый текст на темном фоне
val DarkOnPrimary = Color(0xFF000000) // Черный текст на акценте (PrimaryDark)

// --- СВЕТЛЫЕ ЦВЕТА (LightColorScheme) ---
val PrimaryLight = PrimaryDark // Сохраняем акцентный зеленый
val SecondaryLight = SecondaryDark // Сохраняем глубокий зеленый
val TertiaryLight = TertiaryDark // Сохраняем цвет ошибки/трат

val LightBackground = Color(0xFFFAFAFA) // Очень светлый фон
val LightSurface = Color(0xFFFFFFFF) // Белая поверхность карточек
val LightOnSurface = Color(0xFF1F1F1F) // Темный текст на светлом фоне
val LightOnPrimary = Color(0xFFFFFFFF) // Белый текст на акценте (PrimaryLight)
val LightSurfaceVariant = Color(0xFFEEEEEE) // Слегка серый для карточек/полей
