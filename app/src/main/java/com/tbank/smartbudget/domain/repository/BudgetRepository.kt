package com.tbank.smartbudget.domain.repository

import com.tbank.smartbudget.domain.model.BudgetCategory
import com.tbank.smartbudget.domain.model.BudgetLimitData
import com.tbank.smartbudget.domain.model.BudgetSummary
import com.tbank.smartbudget.domain.model.CategoryLimit

/**
 * Интерфейс-контракт для слоя Data.
 */
interface BudgetRepository {
    // 1. Получение сводки
    suspend fun getActiveBudgetSummary(year: Int, month: Int): Result<BudgetSummary>

    // 2. Получение деталей бюджета по категориям
    suspend fun getCategoryLimits(budgetId: Long): Result<List<CategoryLimit>>

    // 3. Создание/обновление бюджета
    suspend fun saveBudget(
        year: Int,
        month: Int,
        totalIncome: Double,
        limits: List<BudgetLimitData>
    ): Result<Unit>

    // 4. Получение списка всех категорий (для модального окна)
    suspend fun getAllAvailableCategories(): Result<List<BudgetCategory>>

    // 5. Создание новой категории
    suspend fun createCustomCategory(name: String, iconRes: Int, color: Long): Result<BudgetCategory>
}
