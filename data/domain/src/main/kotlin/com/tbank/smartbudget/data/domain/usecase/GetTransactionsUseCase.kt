package com.tbank.smartbudget.data.domain.usecase

import com.tbank.smartbudget.data.domain.model.Transaction
import com.tbank.smartbudget.data.domain.repository.TransactionRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

data class TransactionsResult(
    val totalExpense: Double,
    val groupedTransactions: Map<String, List<Transaction>>,
    val categoryStats: List<CategoryStat>
)

data class CategoryStat(
    val categoryName: String,
    val amount: Double,
    val color: Long,
    val percentage: Float
)

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend fun execute(
        query: String = "",
        startDate: LocalDateTime? = null,
        endDate: LocalDateTime? = null
    ): Result<TransactionsResult> {

        val result = repository.getTransactions(
            page = 0,
            size = 100,
            query = query,
            startDate = startDate,
            endDate = endDate
        )

        return result.map { transactions ->
            val expenses = transactions.filter { it.type.name == "EXPENSE" }
            val totalExpense = expenses.sumOf { it.amount }

            val grouped = transactions
                .sortedByDescending { it.date }
                .groupBy { formatGroupDate(it.date) }

            val stats = expenses
                .groupBy { it.categoryName }
                .map { (name, list) ->
                    val sum = list.sumOf { it.amount }
                    CategoryStat(
                        categoryName = name,
                        amount = sum,
                        color = list.first().categoryColor,
                        percentage = if (totalExpense > 0) (sum / totalExpense).toFloat() else 0f
                    )
                }
                .sortedByDescending { it.amount }

            TransactionsResult(totalExpense, grouped, stats)
        }
    }

    private fun formatGroupDate(date: LocalDateTime): String {
        val now = LocalDateTime.now()
        if (date.toLocalDate() == now.toLocalDate()) return "Сегодня"
        if (date.toLocalDate() == now.minusDays(1).toLocalDate()) return "Вчера"
        val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))
        return date.format(formatter)
    }
}