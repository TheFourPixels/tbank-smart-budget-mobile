package com.tbank.smartbudget.data.domain.repository

import com.tbank.smartbudget.data.domain.model.Transaction
import java.time.LocalDateTime

interface TransactionRepository {
    suspend fun getTransactions(
        page: Int,
        size: Int,
        categoryId: Long? = null,
        query: String? = null,
        startDate: LocalDateTime? = null,
        endDate: LocalDateTime? = null
    ): Result<List<Transaction>>

    suspend fun createTransaction(
        amount: Double,
        type: String, // "INCOME" or "EXPENSE"
        categoryId: Long,
        date: LocalDateTime,
        description: String?,
        merchantName: String?
    ): Result<Transaction>

    suspend fun syncTransactions(year: Int, month: Int): Result<Unit>
}
