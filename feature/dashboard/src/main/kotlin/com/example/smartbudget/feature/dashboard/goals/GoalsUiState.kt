package com.example.smartbudget.feature.dashboard.goals

import com.tbank.smartbudget.core.ui.common.UiState
import com.tbank.smartbudget.data.domain.model.Goal

data class GoalsUiState(
    val isLoading: Boolean = false,
    val goals: List<Goal> = emptyList(),
    val error: String? = null
) : UiState
