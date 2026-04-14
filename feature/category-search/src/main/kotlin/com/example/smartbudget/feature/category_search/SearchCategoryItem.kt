package com.example.smartbudget.feature.category_search

import androidx.compose.ui.graphics.Color
import com.tbank.smartbudget.data.domain.model.CategoryId

/**
 * Модель, оптимизированная для отображения элемента категории в списке поиска.
 * Содержит отформатированные данные.
 */
data class SearchCategoryItem(
    val id: CategoryId,
    val name: String,
    val iconRes: Int,
    val color: Color,
    val limit: String,
    val isTopResult: Boolean
)