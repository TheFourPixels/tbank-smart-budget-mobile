package com.tbank.smartbudget.data.domain.usecase

import com.tbank.smartbudget.data.domain.repository.TransactionRepository
import javax.inject.Inject

class SyncTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend fun execute(year: Int, month: Int): Result<Unit> {
        return repository.syncTransactions(year, month)
    }
}
