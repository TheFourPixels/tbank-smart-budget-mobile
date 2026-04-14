package com.tbank.smartbudget.core.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BudgetDashboardDto(
    @SerialName("month") val month: Int,
    @SerialName("totalIncome") val totalIncome: Double,
    @SerialName("totalSpent") val totalSpent: Double,
    @SerialName("remainingBudget") val remainingBudget: Double,
    @SerialName("categoryStats") val categoryStats: List<DashboardCategoryStat>,
    @SerialName("recentTransactions") val recentTransactions: List<TransactionDto>,
    @SerialName("activeGoals") val activeGoals: List<DashboardGoalStat>
)
@Serializable
data class DashboardCategoryStat(
    @SerialName("categoryId") val categoryId: Long,
    @SerialName("categoryName") val categoryName: String?,
    @SerialName("spentAmount") val spentAmount: Double,
    @SerialName("budgetLimit") val budgetLimit: Double,
    @SerialName("isOverLimit") val isOverLimit: Boolean
)
@Serializable
data class DashboardGoalStat(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("saved") val saved: Double,
    @SerialName("target") val target: Double,
    @SerialName("progressPercent") val progressPercent: Int,
    @SerialName("daysLeft") val daysLeft: Int,
    @SerialName("recommendedMonthly") val recommendedMonthly: Double
)