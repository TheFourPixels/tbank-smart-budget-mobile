package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.domain.model.BudgetCategory
import com.tbank.smartbudget.domain.model.BudgetLimitData
import com.tbank.smartbudget.domain.model.BudgetSummary
import com.tbank.smartbudget.domain.model.CategoryLimit
import com.tbank.smartbudget.domain.repository.BudgetRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Заглушка (Mock) для BudgetRepository.
 * Используется для проверки UI и ViewModel до интеграции с реальным API.
 */
class BudgetRepositoryMockImpl @Inject constructor() : BudgetRepository {

    // Имитация данных бюджета
    private val MOCK_INCOME = 150000.0
    private val MOCK_LIMITS = 135000.0
    private val MOCK_SPENT = 75000.0

    private val MOCK_CATEGORIES = listOf(
        CategoryLimit(
            id = 1, name = "Еда", limitAmount = 40000.0, spentAmount = 35000.0,
            iconRes = 0, color = 0xFF547FF2 // Синий
        ),
        CategoryLimit(
            id = 2, name = "Транспорт", limitAmount = 15000.0, spentAmount = 1000.0,
            iconRes = 0, color = 0xFFFF7A00 // Оранжевый
        ),
        CategoryLimit(
            id = 3, name = "Развлечения", limitAmount = 25000.0, spentAmount = 20000.0,
            iconRes = 0, color = 0xFF4CAF50 // Зеленый
        ),
        CategoryLimit(
            id = 4, name = "Одежда", limitAmount = 55000.0, spentAmount = 19000.0,
            iconRes = 0, color = 0xFFE91E63 // Розовый
        )
    )

    override suspend fun getActiveBudgetSummary(year: Int, month: Int): Result<BudgetSummary> {
        delay(1000) // Имитация задержки сети
        val summary = BudgetSummary(
            totalIncome = MOCK_INCOME,
            totalLimit = MOCK_LIMITS,
            totalSpent = MOCK_SPENT,
            freeFunds = MOCK_INCOME - MOCK_LIMITS
        )
        return Result.success(summary)
    }

    override suspend fun getCategoryLimits(budgetId: Long): Result<List<CategoryLimit>> {
        delay(1500) // Имитация задержки сети
        return Result.success(MOCK_CATEGORIES)
    }

    // Остальные методы-заглушки (для компиляции)
    override suspend fun saveBudget(
        year: Int, month: Int, totalIncome: Double, limits: List<BudgetLimitData>
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getAllAvailableCategories(): Result<List<BudgetCategory>> {
        return Result.success(emptyList())
    }

    override suspend fun createCustomCategory(
        name: String, iconRes: Int, color: Long
    ): Result<BudgetCategory> {
        return Result.failure(Exception("Not implemented yet"))
    }
}
