package com.tbank.smartbudget.core.network.remote.dto

import com.google.gson.annotations.SerializedName

data class BudgetDashboardDto(
    @SerializedName("month") val month: Int,
    @SerializedName("totalIncome") val totalIncome: Double,
    @SerializedName("totalSpent") val totalSpent: Double,
    @SerializedName("remainingBudget") val remainingBudget: Double,
    @SerializedName("categoryStats") val categoryStats: List<DashboardCategoryStat>,
    @SerializedName("recentTransactions") val recentTransactions: List<TransactionDto>,
    @SerializedName("activeGoals") val activeGoals: List<DashboardGoalStat>
)

data class DashboardCategoryStat(
    @SerializedName("categoryId") val categoryId: Long,
    @SerializedName("categoryName") val categoryName: String?,
    @SerializedName("spentAmount") val spentAmount: Double,
    @SerializedName("budgetLimit") val budgetLimit: Double,
    @SerializedName("isOverLimit") val isOverLimit: Boolean
)

data class DashboardGoalStat(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("saved") val saved: Double,
    @SerializedName("target") val target: Double,
    @SerializedName("progressPercent") val progressPercent: Int,
    @SerializedName("daysLeft") val daysLeft: Int,
    @SerializedName("recommendedMonthly") val recommendedMonthly: Double
)