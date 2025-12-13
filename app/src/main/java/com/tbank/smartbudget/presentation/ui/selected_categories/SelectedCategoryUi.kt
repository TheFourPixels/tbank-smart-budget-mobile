package com.tbank.smartbudget.presentation.ui.selected_categories

import androidx.compose.ui.graphics.Color

/**
 * UI-модель категории.
 */
data class SelectedCategoryUi(
    val id: Long,
    val name: String,
    val limitDescription: String, // Например, "Лимит: 2 300 ₽" или "15 операций"
    val color: Color,
    val iconRes: Int = 0
)

/**
 * Состояние экрана "Выбранные категории".
 */
data class SelectedCategoriesUiState(
    val searchQuery: String = "",
    // Категории, которые находятся в верхнем блоке "Выбранные"
    val selectedCategories: List<SelectedCategoryUi> = emptyList(),
    // Категории, которые отображаются ниже (результаты поиска или остальные)
    val availableCategories: List<SelectedCategoryUi> = emptyList()
)