package com.tbank.smartbudget.data.domain.usecase

import com.tbank.smartbudget.data.domain.model.BudgetLimitModel
import com.tbank.smartbudget.data.domain.model.BudgetLimitType
import com.tbank.smartbudget.data.domain.repository.BudgetRepository
import com.tbank.smartbudget.data.domain.repository.CategorySearchRepository
import javax.inject.Inject

/**
 * UseCase для получения всех доступных категорий для экрана добавления транзакции.
 * Объединяет все категории из справочника с текущими лимитами бюджета.
 */
class GetCategoriesForTransactionUseCase @Inject constructor(
    private val categoryRepository: CategorySearchRepository,
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(year: Int, month: Int): Result<List<BudgetLimitModel>> {
        return try {
            // 1. Получаем все существующие в системе категории
            val allCategories = categoryRepository.getAllCategories()
            
            // 2. Получаем статистику по категориям (с текущими тратами)
            val statsResult = budgetRepository.getCategoryStats(year, month)
            val categoryStats = statsResult.getOrNull() ?: emptyList()

            // 3. Мержим: для каждой категории из справочника смотрим, есть ли по ней статистика
            val uiCategories = allCategories.map { category ->
                val stat = categoryStats.find { it.id == category.id }
                BudgetLimitModel(
                    categoryId = category.id,
                    categoryName = category.name,
                    limitValue = stat?.remainingAmount ?: 0.0,
                    limitType = BudgetLimitType.SUM,
                    iconRes = category.iconRes,
                    color = category.color
                )
            }
            Result.success(uiCategories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
