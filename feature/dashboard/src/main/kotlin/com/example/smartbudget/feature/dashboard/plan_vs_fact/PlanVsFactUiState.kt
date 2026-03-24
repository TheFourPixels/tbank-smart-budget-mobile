package com.example.smartbudget.feature.dashboard.plan_vs_fact

import androidx.compose.ui.graphics.Color

data class PlanVsFactUiState(
    val isLoading: Boolean = false,

    // Форматированные строки для отображения
    val totalPlan: String = "0 ₽",
    val totalFact: String = "0 ₽",

    // Название периода (например, "Декабрь 2025")
    val periodName: String = "",

    // Сырые данные для построения графика
    val planValue: Double = 0.0,
    val factValue: Double = 0.0,

    // Процент разницы (для желтой плашки, например "+57,8%")
    val percentageDiffLabel: String = "",

    val categories: List<PlanVsFactCategoryUi> = emptyList()
)

data class PlanVsFactCategoryUi(
    val id: Long,
    val name: String,
    val iconRes: Int,
    val color: Color,
    val planAmount: String,
    val factAmount: String,
    val progress: Float,
    val progressColor: Color
)