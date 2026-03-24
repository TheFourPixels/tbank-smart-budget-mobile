package com.example.smartbudget.feature.dashboard.categories

import androidx.compose.ui.graphics.Color

data class CategoriesDashboardUiState(
    val isLoading: Boolean = false,

    val totalSpent: String = "0 ₽",

    // Данные для круговой диаграммы и списка
    val categories: List<CategoryDashboardItem> = emptyList(),

    // Данные для графика динамики (по дням/неделям)
    val historyData: List<Float> = emptyList()
)

data class CategoryDashboardItem(
    val id: Long,
    val name: String,
    val amountStr: String,
    val amountValue: Double,
    val color: Color,
    val percent: Float, // 0.0 - 1.0
    val iconRes: Int = 0
)