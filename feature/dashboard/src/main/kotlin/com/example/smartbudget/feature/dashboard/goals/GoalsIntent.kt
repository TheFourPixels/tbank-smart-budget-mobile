package com.example.smartbudget.feature.dashboard.goals

import com.tbank.smartbudget.core.ui.common.UiEffect
import com.tbank.smartbudget.core.ui.common.UiIntent

sealed class GoalsIntent : UiIntent {
    data object LoadGoals : GoalsIntent()
    data object OnBackClick : GoalsIntent()
    data object OnAddGoalClick : GoalsIntent()
}

sealed class GoalsEffect : UiEffect {
    data object NavigateBack : GoalsEffect()
    data object NavigateToAddGoal : GoalsEffect()
}
