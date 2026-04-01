package com.tbank.smartbudget.data.domain.model

import java.time.LocalDateTime

enum class TransactionType {
    INCOME, EXPENSE
}

data class Transaction(
    val id: TransactionId,
    val amount: Double,
    val type: TransactionType,
    val date: LocalDateTime,
    val description: String?,
    val merchantName: String?,
    val categoryName: String,
    val categoryColor: Long,
    val categoryId: CategoryId
)