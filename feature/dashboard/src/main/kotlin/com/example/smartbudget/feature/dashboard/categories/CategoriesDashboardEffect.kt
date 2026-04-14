package com.example.smartbudget.feature.dashboard.categories

import com.tbank.smartbudget.core.ui.common.UiEffect

sealed interface CategoriesDashboardEffect : UiEffect {
    data object NavigateBack : CategoriesDashboardEffect
}