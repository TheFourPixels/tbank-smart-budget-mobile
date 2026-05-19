package com.tbank.smartbudget.core.network.remote.api

import com.tbank.smartbudget.core.network.remote.dto.CategorizationRule
import com.tbank.smartbudget.core.network.remote.dto.CategoryTotalSpentDto
import com.tbank.smartbudget.core.network.remote.dto.CreateTransactionRequest
import com.tbank.smartbudget.core.network.remote.dto.PageTransactionDto
import com.tbank.smartbudget.core.network.remote.dto.TransactionDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionApi {

    @GET("api/v1/transactions")
    suspend fun getTransactions(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("categoryId") categoryId: Long? = null,
        @Query("query") query: String? = null,
        @Query("startDateMillis") startDateMillis: Long? = null,
        @Query("endDateMillis") endDateMillis: Long? = null
    ): PageTransactionDto

    @POST("api/v1/transactions")
    suspend fun createTransaction(@Body request: CreateTransactionRequest): TransactionDto

    @POST("api/v1/transactions/sync")
    suspend fun syncTransactions(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Map<String, String>

    @GET("api/v1/rules")
    suspend fun getRules(): List<CategorizationRule>

    @POST("api/v1/rules")
    suspend fun createRule(@Body rule: CategorizationRule): CategorizationRule

    @PATCH("api/v1/transactions/{id}/category")
    suspend fun updateCategory(
        @Path("id") id: Long,
        @Body categoryIdMap: Map<String, Long>
    ): TransactionDto

    @GET("api/v1/transactions/{id}")
    suspend fun details(@Path("id") id: Long): TransactionDto

    @GET("api/v1/transactions/categories/{categoryId}/total")
    suspend fun getTotalSpentByCategory(
        @Path("categoryId") categoryId: Long
    ): CategoryTotalSpentDto

    @DELETE("api/v1/rules/{id}")
    suspend fun deleteRule(@Path("id") id: Long)
}
