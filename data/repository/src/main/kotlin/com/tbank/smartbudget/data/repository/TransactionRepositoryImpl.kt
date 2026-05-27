package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.core.network.remote.api.TransactionApi
import com.tbank.smartbudget.core.network.remote.dto.CategoryDto
import com.tbank.smartbudget.core.network.remote.dto.CreateTransactionRequest
import com.tbank.smartbudget.data.domain.model.CategoryId
import com.tbank.smartbudget.data.domain.model.Transaction
import com.tbank.smartbudget.data.domain.repository.TransactionRepository
import com.tbank.smartbudget.data.repository.mappers.toDomain
import com.tbank.smartbudget.data.repository.utils.TransactionCategorizer
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val api: TransactionApi,
    private val categorizer: TransactionCategorizer,
) : TransactionRepository {

    override suspend fun getTransactions(
        page: Int,
        size: Int,
        categoryId: Long?,
        query: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
    ): Result<List<Transaction>> {
        return try {
            val pageResponse = api.getTransactions(
                page = page,
                size = size,
                categoryId = categoryId,
                query = query,
                startDateMillis = startDate?.toInstant(ZoneOffset.UTC)?.toEpochMilli(),
                endDateMillis = endDate?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
            )

            val rawTransactions = pageResponse.content.map { it.toDomain() }
            val categorized = categorizer.categorize(rawTransactions)

            Result.success(categorized)
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
            val request = CreateTransactionRequest(
                transactionTime = date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z",
                amount = amount,
                type = type,
                merchant = merchantName?.takeIf { it.isNotBlank() },
                categoryId = categoryId,
                category = CategoryDto(id = categoryId),
                description = description?.takeIf { it.isNotBlank() }
            )

            val dto = api.createTransaction(request)
            var domain = dto.toDomain()
            
            // Если сервер вернул транзакцию без категории, используем ID из запроса
            if ((domain.categoryId.value <= 0L) && (categoryId > 0L)) {
                domain = domain.copy(categoryId = CategoryId(categoryId))
            }

            val categorized = categorizer.categorize(listOf(domain)).first()
            Result.success(categorized)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncTransactions(year: Int, month: Int): Result<Unit> {
        return try {
            api.syncTransactions(year, month)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
