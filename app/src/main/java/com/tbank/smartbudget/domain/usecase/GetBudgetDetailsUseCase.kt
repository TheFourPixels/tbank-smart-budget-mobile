package com.tbank.smartbudget.domain.usecase

import com.tbank.smartbudget.domain.model.BudgetDetails
import com.tbank.smartbudget.domain.repository.BudgetRepository
import javax.inject.Inject

class GetBudgetDetailsUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend fun execute(year: Int, month: Int): Result<BudgetDetails> {
        return repository.getBudgetDetails(year, month)
    }
}