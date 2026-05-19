package com.example.smartbudget.feature.dashboard

import com.tbank.smartbudget.core.ui.common.UiState
import com.tbank.smartbudget.data.domain.model.CategoryLimit
import com.tbank.smartbudget.data.domain.model.Goal
import com.tbank.smartbudget.data.domain.model.Transaction

data class BudgetDashboardUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalLimit: String = "0 ₽",
    val totalSpent: String = "0 ₽",
    val totalIncome: String = "0 ₽",
    val remainingAmount: String = "0 ₽",
    val progress: Float = 0f,
    val progressColor: Long = 0xFF43A047,
    val daysLeft: Int = 0,
    val dailyBudget: String = "0 ₽",
    val periodDescription: String = "",
    val recentTransactions: List<Transaction> = emptyList(),
    val activeGoals: List<Goal> = emptyList(),
    val categoryStats: List<CategoryLimit> = emptyList()
) : UiState
