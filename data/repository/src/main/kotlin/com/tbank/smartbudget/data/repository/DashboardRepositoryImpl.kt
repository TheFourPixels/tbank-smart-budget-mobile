package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.core.network.remote.api.DashboardApi
import com.tbank.smartbudget.data.domain.model.CategoryLimit
import com.tbank.smartbudget.data.domain.model.DashboardData
import com.tbank.smartbudget.data.domain.repository.DashboardRepository
import com.tbank.smartbudget.data.repository.mappers.toDomain
import com.tbank.smartbudget.data.repository.utils.TransactionCategorizer
import java.time.LocalDate
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val api: DashboardApi,
    private val categorizer: TransactionCategorizer
) : DashboardRepository {

    override suspend fun getDashboardSummary(month: Int?, year: Int?): Result<DashboardData> {
        return try {
            val now = LocalDate.now()
            val m = month ?: now.monthValue
            val y = year ?: now.year
            
            val dto = api.get(y, m)
            val domainData = dto.toDomain()
            
            // Enrich recent transactions
            val categorizedRecent = categorizer.categorize(domainData.recentTransactions)
            
            // Re-aggregate stats locally to reflect client-side categorization
            // This is a robust fallback when backend stats are not yet updated with new rules
            val aggregatedStats = categorizedRecent.groupBy { it.categoryName }.map { (name, list) ->
                val totalSpent = list.sumOf { it.amount }
                CategoryLimit(
                    id = list.first().categoryId,
                    name = name,
                    limitAmount = 0.0, // Placeholder
                    spentAmount = totalSpent,
                    iconRes = 0,
                    color = list.first().categoryColor
                )
            }

            // Merge local aggregation with backend stats (prioritize backend if available but fill gaps)
            val finalStats = if (domainData.categoryStats.isEmpty()) {
                aggregatedStats
            } else {
                domainData.categoryStats // In real app, we'd merge more complexly
            }
            
            Result.success(domainData.copy(
                recentTransactions = categorizedRecent,
                categoryStats = finalStats
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
