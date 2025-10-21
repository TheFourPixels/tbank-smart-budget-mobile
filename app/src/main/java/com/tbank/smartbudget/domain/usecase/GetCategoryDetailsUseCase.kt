package com.tbank.smartbudget.domain.usecase

import com.tbank.smartbudget.domain.model.CategoryLimit
import com.tbank.smartbudget.domain.repository.BudgetRepository
import javax.inject.Inject

/**
 * Сценарий: Получение деталей бюджета по категориям с сортировкой и расчетом.
 * Logic: Сортировка по степени использования (по убыванию).
 */
class GetCategoryDetailsUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend fun execute(budgetId: Long): Result<List<CategoryLimit>> {
        return repository.getCategoryLimits(budgetId).map { limits ->
            // Сортировка: По степени использования (usagePercentage) по убыванию
            limits.sortedByDescending { it.usagePercentage }
        }
    }
}
