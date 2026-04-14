package com.tbank.smartbudget.feature.budget_tab

import com.tbank.smartbudget.core.ui.common.UiEffect

sealed interface BudgetEffect : UiEffect {
    data object NavigateToBudgetEdit : BudgetEffect
    data object NavigateToSearch : BudgetEffect
    data object NavigateToProfile : BudgetEffect
    data object NavigateToAllOperations : BudgetEffect
    data object NavigateToSelectedCategories : BudgetEffect
}