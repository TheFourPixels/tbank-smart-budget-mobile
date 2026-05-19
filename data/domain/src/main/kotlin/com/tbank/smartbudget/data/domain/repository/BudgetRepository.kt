package com.tbank.smartbudget.data.domain.repository

import com.tbank.smartbudget.data.domain.model.BudgetCategory
import com.tbank.smartbudget.data.domain.model.BudgetDetails
import com.tbank.smartbudget.data.domain.model.BudgetLimitData
import com.tbank.smartbudget.data.domain.model.BudgetSummary
import com.tbank.smartbudget.data.domain.model.CategoryLimit

/**
 * Интерфейс-контракт для слоя Data.
 */
interface BudgetRepository {
    // 1. Получение сводки (Dashboard)
    suspend fun getActiveBudgetSummary(year: Int, month: Int): Result<BudgetSummary>

    // 1a. Получение статистики по категориям (Dashboard)
    suspend fun getCategoryStats(year: Int, month: Int): Result<List<CategoryLimit>>

    // 2. Получение деталей бюджета по категориям (для экрана списка категорий)
    suspend fun getCategoryLimits(budgetId: Long): Result<List<CategoryLimit>>

    // 3. Получение полного бюджета для редактирования
    suspend fun getBudgetDetails(year: Int, month: Int): Result<BudgetDetails>

    // 4. Создание/обновление бюджета
    suspend fun saveBudget(
        year: Int,
        month: Int,
        totalIncome: Double,
        period: String,
        limits: List<BudgetLimitData>
    ): Result<Unit>

    // 5. Удаление бюджета
    suspend fun deleteBudget(year: Int, month: Int): Result<Unit>

    // 6. Получение списка всех категорий
    suspend fun getAllAvailableCategories(): Result<List<BudgetCategory>>

    // 7. Создание новой категории
    suspend fun createCustomCategory(name: String, iconRes: Int, color: Long): Result<BudgetCategory>

    // --- МЕТОДЫ ДЛЯ ВЫБОРА КАТЕГОРИЙ ---
    suspend fun addCategoryToBudget(year: Int, month: Int, categoryId: Long): Result<Unit>
    suspend fun removeCategoryFromBudget(year: Int, month: Int, categoryId: Long): Result<Unit>
}