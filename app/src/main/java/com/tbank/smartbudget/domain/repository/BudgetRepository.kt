package com.tbank.smartbudget.domain.repository

import com.tbank.smartbudget.domain.model.BudgetCategory
import com.tbank.smartbudget.domain.model.BudgetDetails
import com.tbank.smartbudget.domain.model.BudgetLimitData
import com.tbank.smartbudget.domain.model.BudgetSummary
import com.tbank.smartbudget.domain.model.CategoryLimit

/**
 * Интерфейс-контракт для слоя Data.
 */
interface BudgetRepository {
    // 1. Получение сводки (Dashboard)
    suspend fun getActiveBudgetSummary(year: Int, month: Int): Result<BudgetSummary>

    // 2. Получение деталей бюджета по категориям (для экрана списка категорий)
    suspend fun getCategoryLimits(budgetId: Long): Result<List<CategoryLimit>>

    // 3. Получение полного бюджета для редактирования
    suspend fun getBudgetDetails(year: Int, month: Int): Result<BudgetDetails>

    // 4. Создание/обновление бюджета
    suspend fun saveBudget(
        year: Int,
        month: Int,
        totalIncome: Double,
        period: String, // Добавили параметр period
        limits: List<BudgetLimitData>
    ): Result<Unit>

    // 5. Получение списка всех категорий
    suspend fun getAllAvailableCategories(): Result<List<BudgetCategory>>

    // 6. Создание новой категории
    suspend fun createCustomCategory(name: String, iconRes: Int, color: Long): Result<BudgetCategory>

    // --- МЕТОДЫ ДЛЯ ВЫБОРА КАТЕГОРИЙ ---
    suspend fun addCategoryToBudget(year: Int, month: Int, categoryId: Long): Result<Unit>
    suspend fun removeCategoryFromBudget(year: Int, month: Int, categoryId: Long): Result<Unit>
}