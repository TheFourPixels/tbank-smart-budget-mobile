package com.tbank.smartbudget.domain.model

import java.time.LocalDateTime

enum class TransactionType {
    INCOME, EXPENSE
}

data class Transaction(
    val id: Long,
    val amount: Double,
    val type: TransactionType,
    val date: LocalDateTime,
    val description: String?,
    val merchantName: String?,
    val categoryName: String,
    val categoryColor: Long, // Цвет категории для UI
    val categoryId: Long
)