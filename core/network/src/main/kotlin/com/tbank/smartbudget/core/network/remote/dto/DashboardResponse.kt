package com.tbank.smartbudget.core.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
    @SerialName("year") val year: Int,
    @SerialName("month") val month: Int,
    @SerialName("totalIncome") val totalIncome: Double = 0.0,
    @SerialName("budgetPlan") val budgetPlan: Double = 0.0,
    @SerialName("totalSpent") val totalSpent: Double = 0.0,
    @SerialName("remainingBudget") val remainingBudget: Double = 0.0,
    @SerialName("categoryStats") val categoryStats: List<CategoryStatDto>? = null,
    @SerialName("categoriesStats") val categoriesStats: List<CategoryStatDto>? = null,
    @SerialName("recentTransactions") val recentTransactions: List<RecentTransactionDto>? = null,
    @SerialName("activeGoals") val activeGoals: List<GoalSummaryDto>? = null,
)

@Serializable
data class CategoryStatDto(
    @SerialName("categoryId") val categoryId: Long,
    @SerialName("categoryName") val categoryName: String,
    @SerialName("limit") val limit: Double,
    @SerialName("spent") val spent: Double,
    @SerialName("progressPercent") val progressPercent: Int,
    @SerialName("color") val color: String?,
    @SerialName("overLimit") val overLimit: Boolean
)

@Serializable
data class GoalSummaryDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("saved") val saved: Double,
    @SerialName("target") val target: Double,
    @SerialName("progressPercent") val progressPercent: Int,
    @SerialName("daysLeft") val daysLeft: Long,
    @SerialName("recommendedMonthly") val recommendedMonthly: Double
)

@Serializable
data class RecentTransactionDto(
    @SerialName("merchant") val merchant: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("amount") val amount: Double? = null,
    @SerialName("date") val date: String? = null,
    @SerialName("categoryName") val categoryName: String? = null,
    @SerialName("income") val income: Boolean = false
)
