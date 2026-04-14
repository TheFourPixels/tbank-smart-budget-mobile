package com.tbank.smartbudget.feature.budget_edit

import com.tbank.smartbudget.core.ui.common.UiEffect

sealed interface BudgetEditEffect : UiEffect {
    data object NavigateBack : BudgetEditEffect
    data class ShowToast(val message: String) : BudgetEditEffect
}