package com.tbank.smartbudget.data.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class DashboardData(
    val month: Int,
    val totalIncome: Double,
    val totalSpent: Double,
    val remainingBudget: Double,
    val spendingLimit: Double? = null,
    val categoryStats: List<CategoryLimit>,
    val activeGoals: List<Goal>,
    val recentTransactions: List<Transaction>
)
