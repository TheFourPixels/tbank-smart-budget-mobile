package com.example.smartbudget.feature.dashboard.goals

import com.tbank.smartbudget.core.ui.common.UiState
import com.tbank.smartbudget.data.domain.model.Goal

data class GoalDetailsUiState(
    val isLoading: Boolean = false,
    val goal: Goal? = null,
    val error: String? = null,
    val isContributing: Boolean = false
) : UiState
