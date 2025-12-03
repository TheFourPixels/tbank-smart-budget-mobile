package com.tbank.smartbudget.domain.repository

import com.tbank.smartbudget.domain.model.BudgetCategory

/**
 * Репозиторий для получения полного списка категорий, доступных для поиска и выбора.
 */
interface CategorySearchRepository {
    /**
     * Получает все доступные категории.
     * @return Список объектов BudgetCategory.
     */
    suspend fun getAllCategories(): List<BudgetCategory>
}