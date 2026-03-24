package com.tbank.smartbudget.data.domain.usecase

import com.tbank.smartbudget.data.domain.repository.BudgetRepository
import javax.inject.Inject

class DeleteBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend fun execute(year: Int, month: Int): Result<Unit> {
        return repository.deleteBudget(year, month)
    }
}