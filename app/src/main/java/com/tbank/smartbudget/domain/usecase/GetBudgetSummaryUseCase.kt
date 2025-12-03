package com.tbank.smartbudget.domain.usecase

import com.tbank.smartbudget.domain.model.BudgetSummary
import com.tbank.smartbudget.domain.repository.BudgetRepository
import javax.inject.Inject

/**
 * Сценарий: Получение сводной информации для главного экрана.
 */
class GetBudgetSummaryUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    // Здесь будет логика для определения текущего периода
    suspend fun execute(year: Int = 2025, month: Int = 10): Result<BudgetSummary> {
        return repository.getActiveBudgetSummary(year, month)
    }
}
