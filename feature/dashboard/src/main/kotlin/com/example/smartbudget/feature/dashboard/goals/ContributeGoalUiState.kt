package com.example.smartbudget.feature.dashboard.goals

import com.tbank.smartbudget.core.ui.common.UiState

data class ContributeGoalUiState(
    val goalId: Long = 0L,
    val amount: String = "0",
    val cardBalance: Double = 1000.0,
    val cardNumber: String = "• 8563",
    val cardName: String = "Дебетовая карта",
    val isSaving: Boolean = false,
    val error: String? = null
) : UiState
