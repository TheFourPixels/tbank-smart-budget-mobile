package com.tbank.smartbudget.data.domain.usecase

import com.tbank.smartbudget.data.domain.model.CategoryLimit
import com.tbank.smartbudget.data.domain.repository.BudgetRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * UseCase для получения списка всех доступных категорий с информацией о лимитах.
 */
class GetCategoriesForSearchUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    /**
     * Возвращает список категорий с текущими лимитами/тратами.
     */
    suspend operator fun invoke(): List<CategoryLimit> {
        val now = LocalDate.now()
        val statsResult = repository.getCategoryStats(now.year, now.monthValue)
        return statsResult.getOrNull() ?: emptyList()
    }
}
