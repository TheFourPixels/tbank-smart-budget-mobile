package com.tbank.smartbudget.core.network.remote.api

import com.tbank.smartbudget.core.network.remote.dto.TransactionDto
import retrofit2.http.GET
import retrofit2.http.Query

interface BankApi {

    @GET("api/v1/bank/transactions")
    suspend fun fetchTransactions(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): List<TransactionDto>
}
