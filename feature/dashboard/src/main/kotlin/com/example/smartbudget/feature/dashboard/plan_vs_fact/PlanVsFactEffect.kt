package com.example.smartbudget.feature.dashboard.plan_vs_fact

import com.tbank.smartbudget.core.ui.common.UiEffect

sealed interface PlanVsFactEffect : UiEffect {
    data object NavigateBack : PlanVsFactEffect
}