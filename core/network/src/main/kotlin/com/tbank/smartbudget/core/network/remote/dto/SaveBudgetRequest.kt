package com.tbank.smartbudget.core.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO для запроса POST /budgets
 */
@Serializable
data class SaveBudgetRequest(
    @SerialName("year") val year: Int,
    @SerialName("month") val month: Int,
    @SerialName("totalIncome") val totalIncome: Double,
    @SerialName("limits") val limits: List<LimitRequestDto>
)
@Serializable
data class LimitRequestDto(
    @SerialName("categoryId") val categoryId: Long,
    @SerialName("limitValue") val limitValue: Double,
    @SerialName("limitType") val limitType: String
)