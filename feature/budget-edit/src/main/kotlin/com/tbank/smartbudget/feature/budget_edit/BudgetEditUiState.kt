package com.tbank.smartbudget.feature.budget_edit

import com.tbank.smartbudget.core.ui.common.UiState
import com.tbank.smartbudget.data.domain.model.BudgetLimitType

data class BudgetEditUiState(
    val budgetName: String = "Основной",
    val amount: String = "0",
    val categories: List<EditCategoryUi> = emptyList(),
    val periods: List<String> = listOf("1 мес", "2 мес", "3 мес", "Полгода", "Год"),
    val selectedPeriodIndex: Int = 0,
    val isPercentMode: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val isSavedSuccess: Boolean = false,

    val sourceCardBalance: Double = 0.0,
    val sourceCardPan: String = "**** 0000",
    val sourceCardName: String = "T-Bank Black"
) : UiState

data class EditCategoryUi(
    val id: com.tbank.smartbudget.data.domain.model.CategoryId,
    val name: String,
    val limitValue: String,
    val limitType: BudgetLimitType,
    val color: Long
)