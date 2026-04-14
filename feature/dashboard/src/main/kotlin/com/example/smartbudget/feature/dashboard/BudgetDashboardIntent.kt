package com.example.smartbudget.feature.dashboard

import com.tbank.smartbudget.core.ui.common.UiIntent

sealed interface BudgetDashboardIntent : UiIntent {
    data object LoadDashboard : BudgetDashboardIntent
    data class OnChartTypeSelected(val chartType: String) : BudgetDashboardIntent
}