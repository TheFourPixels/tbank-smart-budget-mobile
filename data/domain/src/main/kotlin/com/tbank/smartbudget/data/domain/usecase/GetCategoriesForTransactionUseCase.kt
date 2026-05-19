package com.tbank.smartbudget.data.domain.usecase

import android.util.Log
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
            
            // 3. Получаем детали бюджета для подстраховки (если в статистике нет категории)
            val budgetDetails = budgetRepository.getBudgetDetails(year, month).getOrNull()

            Log.d("GetCategoriesUseCase", "Total categories: ${allCategories.size}, Stats items: ${categoryStats.size}")

            // 4. Мержим: для каждой категории из справочника смотрим, есть ли по ней статистика или лимит
            val uiCategories = allCategories.map { category ->
                // Сначала ищем в статистике (там есть потраченная сумма)
                val stat = categoryStats.find { it.id == category.id }
                
                // Если в статистике нет, ищем в деталях бюджета (там есть установленный лимит)
                val budgetLimit = budgetDetails?.limits?.find { it.categoryId == category.id }
                
                val remainingAmount = when {
                    stat != null -> stat.remainingAmount
                    budgetLimit != null -> budgetLimit.limitValue // Если трат еще нет, остаток = лимиту
                    else -> 0.0
                }

                if (budgetLimit != null || stat != null) {
                    Log.d("GetCategoriesUseCase", "Category ${category.name}: Found limit ${budgetLimit?.limitValue}, Stat remaining: ${stat?.remainingAmount}")
                }

                BudgetLimitModel(
                    categoryId = category.id,
                    categoryName = category.name,
                    limitValue = remainingAmount,
                    limitType = budgetLimit?.limitType ?: BudgetLimitType.SUM,
                    iconRes = category.iconRes,
                    color = category.color
                )
            }
            // Сортируем: сначала те, по которым есть лимиты
            Result.success(uiCategories.sortedByDescending { it.limitValue })
        } catch (e: Exception) {
            Log.e("GetCategoriesUseCase", "Error merging categories", e)
            Result.failure(e)
        }
    }
}
