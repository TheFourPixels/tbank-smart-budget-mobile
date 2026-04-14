package com.example.smartbudget.feature.dashboard

import com.tbank.smartbudget.core.ui.common.UiEffect


sealed interface BudgetDashboardEffect : UiEffect {
    data object NavigateBack : BudgetDashboardEffect
    data object NavigateToPlanVsFact : BudgetDashboardEffect
    data object NavigateToCategoriesDashboard : BudgetDashboardEffect
}