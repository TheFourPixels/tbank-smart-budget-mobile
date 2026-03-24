package com.tbank.smartbudget.data.domain.usecase

import com.tbank.smartbudget.data.domain.model.BudgetDetails
import com.tbank.smartbudget.data.domain.repository.BudgetRepository
import com.tbank.smartbudget.data.domain.repository.CategorySearchRepository
import javax.inject.Inject

/**
 * UseCase берет на себя ответственность за оркестрацию данных.
 * Именно здесь мы мержим данные из BudgetRepository и CategorySearchRepository.
 */
class GetBudgetDetailsUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val categoryRepository: CategorySearchRepository
) {
    suspend operator fun invoke(year: Int, month: Int): Result<BudgetDetails> {
        return try {
            // 1. Получаем сырой бюджет по году и месяцу
            val budgetResult = budgetRepository.getBudgetDetails(year, month)
            if (budgetResult.isFailure) return budgetResult

            val budget = budgetResult.getOrThrow()

            // 2. Получаем список категорий
            val categories = categoryRepository.getAllCategories()

            // 3. Мержим лимиты с категориями (обогащаем данные)
            val enrichedLimits = budget.limits.map { limit ->
                val matchedCategory = categories.find { it.id == limit.categoryId }

                if (matchedCategory != null) {
                    limit.copy(
                        categoryName = matchedCategory.name,
                        iconRes = matchedCategory.iconRes,
                        color = matchedCategory.color
                    )
                } else {
                    limit
                }
            }

            // Возвращаем полностью собранный объект
            Result.success(budget.copy(limits = enrichedLimits))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}