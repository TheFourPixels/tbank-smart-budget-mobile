package com.tbank.smartbudget.data.remote.api

import com.tbank.smartbudget.data.remote.dto.BudgetDashboardDto
import com.tbank.smartbudget.data.remote.dto.BudgetDto
import com.tbank.smartbudget.data.remote.dto.SaveBudgetRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BudgetApi {

    // GET /budgets/{year}/{month}
    @GET("budget/{year}/{month}")
    suspend fun getBudget(
        @Path("year") year: Int,
        @Path("month") month: Int
    ): Response<BudgetDto>

    // DELETE /budgets/{year}/{month}
    @DELETE("budget/{year}/{month}")
    suspend fun deleteBudget(
        @Path("year") year: Int,
        @Path("month") month: Int
    ): Response<Unit>

    // GET /budgets/{year}/{month}/dashboard
    @GET("budget/{year}/{month}/dashboard")
    suspend fun getBudgetDashboard(
        @Path("year") year: Int,
        @Path("month") month: Int
    ): Response<BudgetDashboardDto>

    // POST /budgets
    @POST("budget")
    suspend fun saveBudget(
        @Body request: SaveBudgetRequest
    ): Response<BudgetDto>
}