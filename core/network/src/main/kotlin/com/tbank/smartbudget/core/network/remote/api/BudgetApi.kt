package com.tbank.smartbudget.core.network.remote.api

import com.tbank.smartbudget.core.network.remote.dto.BudgetDashboardDto
import com.tbank.smartbudget.core.network.remote.dto.BudgetDto
import com.tbank.smartbudget.core.network.remote.dto.SaveBudgetRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BudgetApi {

    // GET /budgets/{year}/{month}
    @GET("api/v1/budgets/{year}/{month}")
    suspend fun getBudget(
        @Path("year") year: Int,
        @Path("month") month: Int
    ): BudgetDto

    // DELETE /budgets/{year}/{month}
    @DELETE("api/v1/budgets/{year}/{month}")
    suspend fun deleteBudget(
        @Path("year") year: Int,
        @Path("month") month: Int
    )

    // GET /budgets/{year}/{month}/dashboard
    @GET("api/v1/budgets/{year}/{month}/dashboard")
    suspend fun getBudgetDashboard(
        @Path("year") year: Int,
        @Path("month") month: Int
    ): BudgetDashboardDto

    // POST /budgets
    @POST("api/v1/budgets")
    suspend fun saveBudget(
        @Body request: SaveBudgetRequest
    ): BudgetDto
}