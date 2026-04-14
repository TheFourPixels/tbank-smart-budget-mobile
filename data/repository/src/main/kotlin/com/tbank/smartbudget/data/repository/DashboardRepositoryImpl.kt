package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.core.network.remote.api.DashboardApi
import com.tbank.smartbudget.data.domain.model.CategoryId
import com.tbank.smartbudget.data.domain.model.DashboardData
import com.tbank.smartbudget.data.domain.model.Goal
import com.tbank.smartbudget.data.domain.model.GoalId
import com.tbank.smartbudget.data.domain.model.Transaction
import com.tbank.smartbudget.data.domain.model.TransactionId
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

            val activeGoals = dto.activeGoals.map { goalStat ->
                Goal(
                    id = GoalId(goalStat.id),
                    name = goalStat.name,
                    targetAmount = goalStat.target,
                    savedAmount = goalStat.saved,
                    deadline = null,
                    progressPercent = goalStat.progressPercent
                )
            }

            val recentTransactions = dto.recentTransactions.map { txDto ->
                Transaction(
                    id = TransactionId(txDto.id),
                    amount = txDto.amount,
                    type = if (txDto.isIncome) TransactionType.INCOME else TransactionType.EXPENSE,
                    date = LocalDateTime.parse(txDto.date),
                    description = txDto.description,
                    merchantName = txDto.merchant,
                    categoryName = txDto.categoryName ?: "Без категории",
                    categoryColor = txDto.categoryColor ?: 0xFF808080,
                    categoryId = CategoryId(txDto.categoryId ?: 0L)
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