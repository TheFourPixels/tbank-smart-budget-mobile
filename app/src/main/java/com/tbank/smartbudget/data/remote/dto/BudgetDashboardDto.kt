package com.tbank.smartbudget.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO для ответа /budgets/{year}/{month}/dashboard
 */
data class BudgetDashboardDto(
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("budgetPlan") val budgetPlan: Double,
    @SerializedName("totalSpent") val totalSpent: Double,
    @SerializedName("remainingBudget") val remainingBudget: Double,
    @SerializedName("categoriesStats") val categoriesStats: List<CategoryStatDto>?
)

data class CategoryStatDto(
    @SerializedName("categoryId") val categoryId: Long,
    @SerializedName("spent") val spent: Double,
    @SerializedName("limit") val limit: Double
)