package com.example.smartbudget.feature.dashboard.plan_vs_fact

import androidx.compose.ui.graphics.Color
import com.tbank.smartbudget.core.ui.common.UiState
import com.tbank.smartbudget.data.domain.model.CategoryId

data class PlanVsFactUiState(
    val isLoading: Boolean = false,
    val totalPlan: String = "0 ₽",
    val totalFact: String = "0 ₽",
    val periodName: String = "",
    val planValue: Double = 0.0,
    val factValue: Double = 0.0,
    val dailyLimit: Float = 0f,
    val daysInMonth: Int = 30,
    val percentageDiffLabel: String = "",
    val expenseHistory: List<Float> = emptyList(),
    val categories: List<PlanVsFactCategoryUi> = emptyList()
) : UiState

data class PlanVsFactCategoryUi(
    val id: CategoryId,
    val name: String,
    val iconRes: Int,
    val color: Color,
    val planAmount: String,
    val factAmount: String,
    val progress: Float,
    val progressColor: Color
)