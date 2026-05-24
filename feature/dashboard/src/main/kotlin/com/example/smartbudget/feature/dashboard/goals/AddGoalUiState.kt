package com.example.smartbudget.feature.dashboard.goals

import com.tbank.smartbudget.core.ui.common.UiState
import java.time.LocalDate

data class AddGoalUiState(
    val name: String = "",
    val amount: String = "0",
    val deadline: LocalDate = LocalDate.now().plusMonths(6),
    val recommendedMonthly: Double = 0.0,
    val isSaving: Boolean = false,
    val error: String? = null
) : UiState
