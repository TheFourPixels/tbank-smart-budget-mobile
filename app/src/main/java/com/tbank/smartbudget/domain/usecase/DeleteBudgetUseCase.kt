package com.tbank.smartbudget.domain.usecase

import com.tbank.smartbudget.domain.repository.BudgetRepository
import javax.inject.Inject

class DeleteBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend fun execute(year: Int, month: Int): Result<Unit> {
        return repository.deleteBudget(year, month)
    }
}