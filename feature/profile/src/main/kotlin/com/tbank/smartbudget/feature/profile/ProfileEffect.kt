package com.tbank.smartbudget.feature.profile

import com.tbank.smartbudget.core.ui.common.UiEffect

sealed interface ProfileEffect : UiEffect {
    data class NavigateToBudgetDetails(val budgetId: Long) : ProfileEffect
}