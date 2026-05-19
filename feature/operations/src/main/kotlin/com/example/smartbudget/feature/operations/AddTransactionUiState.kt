package com.example.smartbudget.feature.operations

import com.tbank.smartbudget.core.ui.common.UiState
import com.tbank.smartbudget.data.domain.model.BudgetLimitModel
import com.tbank.smartbudget.data.domain.model.TransactionType
import java.time.LocalDateTime

data class AddTransactionUiState(
    val amount: String = "0",
    val type: TransactionType = TransactionType.EXPENSE,
    val selectedCategoryId: Long? = null,
    val categories: List<BudgetLimitModel> = emptyList(),
    val date: LocalDateTime = LocalDateTime.now(),
    val merchantName: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
) : UiState
