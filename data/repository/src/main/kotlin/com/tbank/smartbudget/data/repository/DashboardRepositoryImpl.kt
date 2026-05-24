package com.tbank.smartbudget.data.repository

import android.util.Log
import com.tbank.smartbudget.core.network.remote.api.DashboardApi
import com.tbank.smartbudget.data.domain.model.CategoryLimit
import com.tbank.smartbudget.data.domain.model.DashboardData
import com.tbank.smartbudget.data.domain.model.TransactionType
import com.tbank.smartbudget.data.domain.repository.DashboardRepository
import com.tbank.smartbudget.data.repository.mappers.toDomain
import com.tbank.smartbudget.data.repository.utils.TransactionCategorizer
import java.time.LocalDate
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val api: DashboardApi,
    private val transactionRepository: com.tbank.smartbudget.data.domain.repository.TransactionRepository,
    private val categorizer: TransactionCategorizer
) : DashboardRepository {

    override suspend fun getDashboardSummary(month: Int?, year: Int?): Result<DashboardData> {
        return try {
            val now = LocalDate.now()
            val m = month ?: now.monthValue
            val y = year ?: now.year
            
            val dto = api.get(y, m)
            val domainData = dto.toDomain()
            
            // Fetch real transactions for the requested month to ensure accurate 'spent' data
            val startOfMonth = LocalDate.of(y, m, 1).atStartOfDay()
            val endOfMonth = LocalDate.of(y, m, 1).plusMonths(1).atStartOfDay().minusNanos(1)
            
            val realTransactions = transactionRepository.getTransactions(
                page = 0, 
                size = 100,
                startDate = startOfMonth,
                endDate = endOfMonth
            ).getOrNull() ?: emptyList()
            
            // Enrich recent transactions
            val transactionsToCategorize = if (domainData.recentTransactions.all { it.amount == 0.0 }) {
                realTransactions // Use real transactions if dashboard returns empty/dummy ones
            } else {
                domainData.recentTransactions
            }
            
            val categorizedRecent = categorizer.categorize(transactionsToCategorize)
            
            // Re-aggregate stats locally from recent transactions to reflect real-time changes
            val localAggregatedSpent = categorizedRecent
                .filter { it.type == TransactionType.EXPENSE }
                .groupBy { it.categoryId }
                .mapValues { (_, txs) -> txs.sumOf { it.amount } }

            Log.d("DashboardRepo", "Local aggregation: ${localAggregatedSpent.size} categories found in recent transactions")
            localAggregatedSpent.forEach { (id, amount) -> Log.d("DashboardRepo", "   - Category ID $id: Spent $amount") }

            // Merge local aggregation with backend stats
            val mergedStats = domainData.categoryStats.map { backendStat ->
                val localSpent = localAggregatedSpent[backendStat.id] ?: 0.0
                val finalSpent = maxOf(backendStat.spentAmount, localSpent)
                Log.d("DashboardRepo", "Merging ${backendStat.name}: Backend=${backendStat.spentAmount}, Local=$localSpent -> Final=$finalSpent")
                backendStat.copy(spentAmount = finalSpent)
            }.toMutableList()

            // Add categories that might be in recent transactions but not in backend stats
            localAggregatedSpent.forEach { (catId, spent) ->
                if (mergedStats.none { it.id == catId }) {
                    val tx = categorizedRecent.firstOrNull { it.categoryId == catId }
                    if (tx != null) {
                        mergedStats.add(
                            CategoryLimit(
                                id = catId,
                                name = tx.categoryName,
                                limitAmount = 0.0,
                                spentAmount = spent,
                                iconRes = 0,
                                color = tx.categoryColor
                            )
                        )
                    }
                }
            }

            // Recalculate totalSpent and remainingBudget based on merged stats
            val totalSpent = if (mergedStats.isNotEmpty()) mergedStats.sumOf { it.spentAmount } else domainData.totalSpent
            
            // If totalIncome is 0, we try to recover it from remaining + spent as a last resort
            val finalIncome = if (domainData.totalIncome > 0) domainData.totalIncome else (domainData.remainingBudget + totalSpent)
            val remainingBudget = (finalIncome - totalSpent).coerceAtLeast(0.0)
            
            Log.d("DashboardRepo", "Returning ${mergedStats.size} category stats. Total spent: $totalSpent")
            mergedStats.forEach { Log.d("DashboardRepo", "   -> Stat: ${it.name} ID=${it.id.value} Spent=${it.spentAmount}") }

            Result.success(domainData.copy(
                totalIncome = finalIncome,
                totalSpent = totalSpent,
                remainingBudget = remainingBudget,
                recentTransactions = categorizedRecent,
                categoryStats = mergedStats.sortedByDescending { it.spentAmount }
            ))
        } catch (e: Exception) {
            Log.e("DashboardRepo", "Error loading dashboard", e)
            Result.failure(e)
        }
    }
}
