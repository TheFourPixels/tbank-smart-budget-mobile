package com.example.smartbudget.feature.dashboard.goals

import com.tbank.smartbudget.core.ui.common.UiEffect
import com.tbank.smartbudget.core.ui.common.UiIntent

sealed class GoalDetailsIntent : UiIntent {
    data class LoadGoal(val id: Long) : GoalDetailsIntent()
    data class OnContributeClicked(val amount: Double) : GoalDetailsIntent()
    data object OnBackClicked : GoalDetailsIntent()
}

sealed class GoalDetailsEffect : UiEffect {
    data object NavigateBack : GoalDetailsEffect()
}
