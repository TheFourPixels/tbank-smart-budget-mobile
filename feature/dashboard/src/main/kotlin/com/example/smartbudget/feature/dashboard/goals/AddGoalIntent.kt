package com.example.smartbudget.feature.dashboard.goals

import com.tbank.smartbudget.core.ui.common.UiEffect
import com.tbank.smartbudget.core.ui.common.UiIntent
import java.time.LocalDate

sealed class AddGoalIntent : UiIntent {
    data class OnNameChanged(val name: String) : AddGoalIntent()
    data class OnAmountChanged(val amount: String) : AddGoalIntent()
    data class OnDeadlineChanged(val date: LocalDate) : AddGoalIntent()
    data object OnSaveClicked : AddGoalIntent()
    data object OnBackClicked : AddGoalIntent()
}

sealed class AddGoalEffect : UiEffect {
    data object NavigateBack : AddGoalEffect()
}
