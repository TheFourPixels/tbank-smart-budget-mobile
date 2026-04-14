package com.example.smartbudget.feature.dashboard.categories

import com.tbank.smartbudget.core.ui.common.UiIntent

sealed interface CategoriesDashboardIntent : UiIntent {
    data object LoadData : CategoriesDashboardIntent
}