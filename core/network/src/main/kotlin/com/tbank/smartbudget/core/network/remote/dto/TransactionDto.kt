package com.tbank.smartbudget.core.network.remote.dto

import com.google.gson.annotations.SerializedName

data class TransactionDto(
    @SerializedName("id") val id: Long,
    @SerializedName("amount") val amount: Double,
    @SerializedName("income") val isIncome: Boolean,
    @SerializedName("date") val date: String,
    @SerializedName("description") val description: String?,
    @SerializedName("categoryName") val categoryName: String?,
    @SerializedName("categoryColor") val categoryColor: Long?,
    @SerializedName("categoryId") val categoryId: Long?,
    @SerializedName("merchant") val merchant: String?
)

data class CreateTransactionRequest(
    @SerializedName("amount") val amount: Double,
    @SerializedName("income") val isIncome: Boolean,
    @SerializedName("date") val date: String,
    @SerializedName("description") val description: String?,
    @SerializedName("categoryId") val categoryId: Long,
    @SerializedName("budgetId") val budgetId: Long,
    @SerializedName("merchant") val merchant: String?
)