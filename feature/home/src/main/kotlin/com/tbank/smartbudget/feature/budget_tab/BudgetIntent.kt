package com.tbank.smartbudget.feature.budget_tab

import com.tbank.smartbudget.core.ui.common.UiIntent

sealed interface BudgetIntent : UiIntent {
    data object LoadData : BudgetIntent
    data object OnRefresh : BudgetIntent
    data object OnBudgetClick : BudgetIntent
    data object OnSearchClick : BudgetIntent
    data object OnProfileClick : BudgetIntent
    data object OnAllOperationsClick : BudgetIntent
    data object OnSelectedCategoriesClick : BudgetIntent
}
