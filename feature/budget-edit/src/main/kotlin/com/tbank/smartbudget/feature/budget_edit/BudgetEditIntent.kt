package com.tbank.smartbudget.feature.budget_edit

import com.tbank.smartbudget.core.ui.common.UiIntent

sealed interface BudgetEditIntent : UiIntent {
    data object LoadBudget : BudgetEditIntent
    data object RefreshCategories : BudgetEditIntent
    data object ToggleGlobalLimitType : BudgetEditIntent
    data class OnPeriodSelected(val index: Int) : BudgetEditIntent
    data class OnAmountChanged(val newAmount: String) : BudgetEditIntent
    data class OnCategoryLimitChanged(val categoryId: Long, val newValue: String) : BudgetEditIntent
    data class OnCategoryTypeToggle(val categoryId: Long) : BudgetEditIntent
    data object OnSaveClicked : BudgetEditIntent
    data object OnDeleteClicked : BudgetEditIntent
    data object ClearError : BudgetEditIntent
    data object ResetSuccess : BudgetEditIntent
}