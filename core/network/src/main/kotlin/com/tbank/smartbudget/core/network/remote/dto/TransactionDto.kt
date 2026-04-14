package com.tbank.smartbudget.core.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    @SerialName("id") val id: Long,
    @SerialName("amount") val amount: Double,
    @SerialName("income") val isIncome: Boolean,
    @SerialName("date") val date: String,
    @SerialName("description") val description: String?,
    @SerialName("categoryName") val categoryName: String?,
    @SerialName("categoryColor") val categoryColor: Long?,
    @SerialName("categoryId") val categoryId: Long?,
    @SerialName("merchant") val merchant: String?
)
@Serializable
data class CreateTransactionRequest(
    @SerialName("amount") val amount: Double,
    @SerialName("income") val isIncome: Boolean,
    @SerialName("date") val date: String,
    @SerialName("description") val description: String?,
    @SerialName("categoryId") val categoryId: Long,
    @SerialName("budgetId") val budgetId: Long,
    @SerialName("merchant") val merchant: String?
)