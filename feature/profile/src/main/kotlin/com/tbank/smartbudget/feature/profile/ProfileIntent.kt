package com.tbank.smartbudget.feature.profile

import com.tbank.smartbudget.core.ui.common.UiIntent

sealed interface ProfileIntent : UiIntent {
    data object LoadProfile : ProfileIntent
    data class OnBudgetSelected(val budgetId: Long) : ProfileIntent
    data object OnEditNameClicked : ProfileIntent
    data class OnNameChanged(val newName: String) : ProfileIntent
    data object OnSaveNameClicked : ProfileIntent
    data object OnCancelEditClicked : ProfileIntent
}
