package com.tbank.smartbudget.core.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    @SerialName("id") val id: Long,
    @SerialName("amount") val amount: Double,
    @SerialName("type") val type: String, // INCOME/EXPENSE
    @SerialName("externalId") val externalId: String? = null,
    @SerialName("transactionDate") val transactionDate: String, // date-time
    @SerialName("description") val description: String?,
    @SerialName("merchantName") val merchantName: String?,
    @SerialName("mcc") val mcc: String? = null,
    @SerialName("category") val category: CategoryDto?,
    @SerialName("isIncome") val isIncome: Boolean,
    @SerialName("splits") val splits: List<SplitPartDto>? = null
)

@Serializable
data class SplitPartDto(
    @SerialName("categoryId") val categoryId: Long,
    @SerialName("amount") val amount: Double,
    @SerialName("description") val description: String?
)

@Serializable
data class CreateTransactionRequest(
    @SerialName("transactionTime") val transactionTime: String,
    @SerialName("amount") val amount: Double,
    @SerialName("type") val type: String,
    @SerialName("merchant") val merchant: String?,
    @SerialName("categoryId") val categoryId: Long,
    @SerialName("category") val category: CategoryDto? = null,
    @SerialName("description") val description: String?
)

@Serializable
data class CategorizationRule(
    @SerialName("id") val id: Long? = null,
    @SerialName("userId") val userId: Long? = null,
    @SerialName("keyword") val keyword: String,
    @SerialName("categoryId") val categoryId: Long
)

@Serializable
data class CategoryTotalSpentDto(
    @SerialName("categoryId") val categoryId: Long,
    @SerialName("totalSpent") val totalSpent: Double
)

@Serializable
data class PageTransactionDto(
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("totalElements") val totalElements: Long,
    @SerialName("size") val size: Int,
    @SerialName("content") val content: List<TransactionDto>,
    @SerialName("number") val number: Int,
    @SerialName("numberOfElements") val numberOfElements: Int,
    @SerialName("first") val first: Boolean,
    @SerialName("last") val last: Boolean,
    @SerialName("empty") val empty: Boolean
)
