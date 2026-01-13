package com.tbank.smartbudget.presentation.ui.profile

import androidx.compose.ui.graphics.Color

data class ProfileUiState(
    val userName: String = "",
    val userAvatarUrl: String? = null,
    val budgets: List<BudgetProfileItem> = emptyList(),
    val isLoading: Boolean = false
)

data class BudgetProfileItem(
    val id: Long,
    val name: String,
    val dateDescription: String,
    val color: Color,
    val initial: String
)