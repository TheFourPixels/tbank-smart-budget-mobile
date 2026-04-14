package com.example.smartbudget.feature.dashboard.categories

import androidx.compose.ui.graphics.Color
import com.tbank.smartbudget.core.ui.common.UiState

data class CategoriesDashboardUiState(
    val isLoading: Boolean = false,
    val totalSpent: String = "0 ₽",
    val categories: List<CategoryDashboardItem> = emptyList(),
    val historyData: List<Float> = emptyList()
) : UiState

data class CategoryDashboardItem(
    val id: Long,
    val name: String,
    val amountStr: String,
    val amountValue: Double,
    val color: Color,
    val percent: Float,
    val iconRes: Int = 0
)