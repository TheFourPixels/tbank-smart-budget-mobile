package com.tbank.smartbudget.presentation.ui.category_search

import androidx.compose.ui.graphics.Color

/**
 * Модель, оптимизированная для отображения элемента категории в списке поиска.
 * Содержит отформатированные данные.
 */
data class SearchCategoryItem(
    val id: Long,
    val name: String,
    val iconRes: Int,
    val color: Color,
    val limit: String,
    val isTopResult: Boolean
)