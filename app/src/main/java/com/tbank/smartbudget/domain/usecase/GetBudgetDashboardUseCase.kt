package com.tbank.smartbudget.domain.usecase

import com.tbank.smartbudget.domain.model.BudgetSummary
import com.tbank.smartbudget.domain.repository.BudgetRepository
import javax.inject.Inject

/**
 * Получает сводные данные для экрана расчетов (Dashboard).
 */
class GetBudgetDashboardUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend fun execute(year: Int, month: Int): Result<BudgetSummary> {
        return repository.getActiveBudgetSummary(year, month)
    }
}