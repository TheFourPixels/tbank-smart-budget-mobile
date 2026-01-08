package com.tbank.smartbudget.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO для ответа /budgets/{year}/{month}
 */
data class BudgetDto(
    @SerializedName("id") val id: Long,
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("totalIncome") val totalIncome: Double,
    @SerializedName("limits") val limits: List<BudgetLimitDto>
)

data class BudgetLimitDto(
    @SerializedName("categoryId") val categoryId: Long,
    @SerializedName("limitValue") val limitValue: Double,
    @SerializedName("limitType") val limitType: String // "PERCENT" или "AMOUNT"
)