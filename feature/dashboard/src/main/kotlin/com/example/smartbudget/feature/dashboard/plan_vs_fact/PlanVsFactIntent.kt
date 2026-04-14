package com.example.smartbudget.feature.dashboard.plan_vs_fact

import com.tbank.smartbudget.core.ui.common.UiIntent

sealed interface PlanVsFactIntent : UiIntent {
    data object LoadData : PlanVsFactIntent
}