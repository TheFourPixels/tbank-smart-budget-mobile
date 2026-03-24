package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.core.network.remote.api.TransactionApi
import com.tbank.smartbudget.core.network.remote.dto.CreateTransactionRequest
import com.tbank.smartbudget.data.domain.model.Transaction
import com.tbank.smartbudget.data.domain.model.TransactionType
import com.tbank.smartbudget.data.domain.repository.TransactionRepository
import java.time.LocalDateTime
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val api: TransactionApi
) : TransactionRepository {

    override suspend fun getTransactions(
        page: Int,
        size: Int,
        categoryId: Long?,
        query: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): Result<List<Transaction>> {
        return try {
            val pageResponse = api.getTransactions(
                page = page,
                size = size,
                categoryId = categoryId,
                query = query,
                startDate = startDate?.toString(),
                endDate = endDate?.toString()
            )

            val transactions = pageResponse.content.map { dto ->
                Transaction(
                    id = dto.id,
                    amount = dto.amount,
                    type = if (dto.isIncome) TransactionType.INCOME else TransactionType.EXPENSE,
                    // Парсим дату из строки ISO в LocalDateTime
                    date = LocalDateTime.parse(dto.date),
                    description = dto.description,
                    merchantName = dto.merchant,
                    categoryName = dto.categoryName ?: "Без категории",
                    categoryColor = dto.categoryColor ?: 0xFF808080, // Серый по умолчанию
                    categoryId = dto.categoryId ?: 0L
                )
            }
            Result.success(transactions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createTransaction(
        amount: Double,
        type: String,
        categoryId: Long,
        date: LocalDateTime,
        description: String?,
        merchantName: String?
    ): Result<Transaction> {
        return try {
            val isIncome = type == "INCOME"

            val request = CreateTransactionRequest(
                amount = amount,
                isIncome = isIncome,
                date = date.toString(),
                description = description,
                categoryId = categoryId,
                budgetId = 0,
                merchant = merchantName
            )

            val dto = api.createTransaction(request)

            val transaction = Transaction(
                id = dto.id,
                amount = dto.amount,
                type = if (dto.isIncome) TransactionType.INCOME else TransactionType.EXPENSE,
                date = LocalDateTime.parse(dto.date),
                description = dto.description,
                merchantName = dto.merchant,
                categoryName = dto.categoryName ?: "Без категории",
                categoryColor = dto.categoryColor ?: 0xFF808080,
                categoryId = dto.categoryId ?: 0L
            )
            Result.success(transaction)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}