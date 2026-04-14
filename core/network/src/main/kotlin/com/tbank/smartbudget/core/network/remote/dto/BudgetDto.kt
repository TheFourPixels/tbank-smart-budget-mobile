package com.tbank.smartbudget.core.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO для ответа /budgets/{year}/{month}
 */
@Serializable
data class BudgetDto(
    @SerialName("id") val id: Long,
    @SerialName("year") val year: Int,
    @SerialName("month") val month: Int,
    @SerialName("totalIncome") val totalIncome: Double,
    @SerialName("limits") val limits: List<BudgetLimitDto>
)
@Serializable
data class BudgetLimitDto(
    @SerialName("categoryId") val categoryId: Long,
    @SerialName("limitValue") val limitValue: Double,
    @SerialName("limitType") val limitType: String
)