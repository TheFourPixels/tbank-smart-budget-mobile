package com.tbank.smartbudget.data.domain.model

/**
 * Полная информация о бюджете на конкретный месяц.
 * Соответствует ответу GET /budgets/{year}/{month}
 */

@JvmInline value class BudgetId(val value: Long)
@JvmInline value class TransactionId(val value: Long)


data class BudgetDetails(
    val id: BudgetId,
    val year: Int,
    val month: Int,
    val totalIncome: Double,
    val period: String,
    val limits: List<BudgetLimitModel>
)

/**
 * Модель лимита категории внутри бюджета.
 */
data class BudgetLimitModel(
    val categoryId: CategoryId,
    val categoryName: String,
    val limitValue: Double,
    val limitType: BudgetLimitType, // PERCENT или AMOUNT
    val iconRes: Int = 0,
    val color: Long = 0xFF42A5F5
)

enum class BudgetLimitType {
    PERCENT, SUM
}
