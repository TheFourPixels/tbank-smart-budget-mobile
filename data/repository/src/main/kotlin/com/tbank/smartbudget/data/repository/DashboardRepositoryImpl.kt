package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.core.network.remote.api.DashboardApi
import com.tbank.smartbudget.data.domain.model.DashboardData
import com.tbank.smartbudget.data.domain.model.Goal
import com.tbank.smartbudget.data.domain.model.Transaction
import com.tbank.smartbudget.data.domain.model.TransactionType
import com.tbank.smartbudget.data.domain.repository.DashboardRepository
import java.time.LocalDateTime
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val api: DashboardApi
) : DashboardRepository {

    override suspend fun getDashboardSummary(month: Int?, year: Int?): Result<DashboardData> {
        return try {
            val dto = api.getDashboardSummary(month, year)

            // Маппинг целей
            val activeGoals = dto.activeGoals.map { goalStat ->
                Goal(
                    id = goalStat.id,
                    name = goalStat.name,
                    targetAmount = goalStat.target,
                    savedAmount = goalStat.saved,
                    deadline = null, // В сводке дедлайн обычно не передается
                    progressPercent = goalStat.progressPercent
                )
            }

            // Маппинг транзакций с учетом новой доменной модели
            val recentTransactions = dto.recentTransactions.map { txDto ->
                Transaction(
                    id = txDto.id,
                    amount = txDto.amount,
                    // Конвертируем Boolean income в Enum TransactionType
                    type = if (txDto.isIncome) TransactionType.INCOME else TransactionType.EXPENSE,
                    // Парсим строку даты в LocalDateTime
                    date = LocalDateTime.parse(txDto.date),
                    description = txDto.description,
                    merchantName = txDto.merchant,
                    categoryName = txDto.categoryName ?: "Без категории",
                    // Используем новые поля из DTO или значения по умолчанию
                    categoryColor = txDto.categoryColor ?: 0xFF808080,
                    categoryId = txDto.categoryId ?: 0L
                )
            }

            val dashboardData = DashboardData(
                month = dto.month,
                totalIncome = dto.totalIncome,
                totalSpent = dto.totalSpent,
                remainingBudget = dto.remainingBudget,
                activeGoals = activeGoals,
                recentTransactions = recentTransactions
            )

            Result.success(dashboardData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}