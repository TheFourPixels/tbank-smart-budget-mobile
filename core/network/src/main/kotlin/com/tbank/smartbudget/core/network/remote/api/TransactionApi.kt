package com.tbank.smartbudget.core.network.remote.api

import com.tbank.smartbudget.core.network.remote.dto.CreateTransactionRequest
import com.tbank.smartbudget.core.network.remote.dto.PageDto
import com.tbank.smartbudget.core.network.remote.dto.TransactionDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionApi {

    @GET("transactions")
    suspend fun getTransactions(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("categoryId") categoryId: Long?,
        @Query("query") query: String?,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): PageDto<TransactionDto>

    @POST("transactions")
    suspend fun createTransaction(@Body request: CreateTransactionRequest): TransactionDto

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(@Path("id") id: Long)
}