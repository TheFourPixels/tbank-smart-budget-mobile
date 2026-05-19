package com.tbank.smartbudget.feature.budget_tab

import com.tbank.smartbudget.core.ui.common.UiState
import com.tbank.smartbudget.data.domain.model.BudgetPeriod
import com.tbank.smartbudget.data.domain.model.CategoryId

data class BudgetUiState(
    val period: BudgetPeriod = BudgetPeriod.MONTH,
    val userName: String = "",
    val budgetName: String = "Кубышка",
    val budgetTerm: String = "",
    val summary: BudgetSummaryUi? = null,
    val categories: List<CategoryUi> = emptyList(),
    val hasBudget: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState

data class BudgetSummaryUi(
    val totalIncome: String,
    val totalLimit: String,
    val totalSpent: String,
    val freeFunds: String,
    val progress: Float
)

data class CategoryUi(
    val id: CategoryId,
    val name: String,
    val iconRes: Int,
    val color: Long,
    val spentValue: String,
    val limitValue: String,
    val progress: Float
)
