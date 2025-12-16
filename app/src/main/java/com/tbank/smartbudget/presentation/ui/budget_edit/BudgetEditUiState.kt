package com.tbank.smartbudget.presentation.ui.budget_edit

import com.tbank.smartbudget.domain.model.BudgetLimitType

data class BudgetEditUiState(
    val budgetName: String = "Кубышка",
    val selectedPeriodIndex: Int = 0,
    val periods: List<String> = listOf("2 мес", "3 мес", "4 мес", "6 мес", "Другой"),

    val amount: String = "", // Сумма общего дохода
    val currency: String = "Рубли",

    val sourceCardName: String = "Дебетовая карта",
    val sourceCardPan: String = "• 8563",
    val sourceCardBalance: String = "1 000 ₽",

    val categories: List<EditCategoryUi> = emptyList(),

    // Состояния экрана
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSavedSuccess: Boolean = false,
    val error: String? = null
)

data class EditCategoryUi(
    val id: Long,
    val name: String,
    val limitValue: String, // Строка для TextField
    val limitType: BudgetLimitType, // PERCENT или AMOUNT
    val color: Long
)