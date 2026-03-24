package com.tbank.smartbudget.core.network.remote.api

import com.tbank.smartbudget.core.network.remote.dto.BudgetDashboardDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DashboardApi {

    @GET("api/v1/dashboard/summary")
    suspend fun getDashboardSummary(
        @Query("month") month: Int? = null,
        @Query("year") year: Int? = null
    ): BudgetDashboardDto
}