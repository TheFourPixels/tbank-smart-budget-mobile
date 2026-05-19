package com.tbank.smartbudget.feature.profile

import androidx.compose.ui.graphics.Color
import com.tbank.smartbudget.core.ui.common.UiState

data class ProfileUiState(
    val userName: String = "",
    val editingName: String = "",
    val isEditingName: Boolean = false,
    val userAvatarUrl: String? = null,
    val budgets: List<BudgetProfileItem> = emptyList(),
    val isLoading: Boolean = false
) : UiState

data class BudgetProfileItem(
    val id: Long,
    val name: String,
    val dateDescription: String,
    val initial: String,
    val color: Color = Color.Gray
)