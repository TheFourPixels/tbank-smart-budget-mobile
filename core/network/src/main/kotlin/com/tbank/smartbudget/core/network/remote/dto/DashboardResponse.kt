package com.tbank.smartbudget.core.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
    @SerialName("year") val year: Int,
    @SerialName("month") val month: Int,
    @SerialName("totalIncome") val totalIncome: Double,
    @SerialName("totalSpent") val totalSpent: Double,
    @SerialName("remainingBudget") val remainingBudget: Double,
    @SerialName("categoryStats") val categoryStats: List<CategoryStatDto>,
    @SerialName("recentTransactions") val recentTransactions: List<RecentTransactionDto>,
    @SerialName("activeGoals") val activeGoals: List<GoalSummaryDto>
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
    @SerialName("merchant") val merchant: String?,
    @SerialName("description") val description: String?,
    @SerialName("amount") val amount: Double,
    @SerialName("date") val date: String, // date-time
    @SerialName("categoryName") val categoryName: String?,
    @SerialName("income") val income: Boolean
)
