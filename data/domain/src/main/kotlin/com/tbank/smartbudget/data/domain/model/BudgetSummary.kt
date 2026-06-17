package com.tbank.smartbudget.data.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class BudgetSummary(
    val totalIncome: Double,
    val totalLimit: Double,
    val totalSpent: Double,
    val spendingLimit: Double? = null,
    val freeFunds: Double, // totalIncome - totalLimit
    val period: String
)
