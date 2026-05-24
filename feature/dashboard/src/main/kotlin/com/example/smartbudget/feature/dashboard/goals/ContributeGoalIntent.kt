package com.example.smartbudget.feature.dashboard.goals

import com.tbank.smartbudget.core.ui.common.UiEffect
import com.tbank.smartbudget.core.ui.common.UiIntent

sealed class ContributeGoalIntent : UiIntent {
    data class Init(val goalId: Long, val amount: Double) : ContributeGoalIntent()
    data class OnAmountChanged(val amount: String) : ContributeGoalIntent()
    data object OnContributeClicked : ContributeGoalIntent()
    data object OnBackClicked : ContributeGoalIntent()
}

sealed class ContributeGoalEffect : UiEffect {
    data object NavigateBack : ContributeGoalEffect()
}
