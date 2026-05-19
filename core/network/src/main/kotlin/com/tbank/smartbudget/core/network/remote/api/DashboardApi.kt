package com.tbank.smartbudget.core.network.remote.api

import com.tbank.smartbudget.core.network.remote.dto.DashboardResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DashboardApi {

    @GET("api/v1/dashboard/{year}/{month}")
    suspend fun get(
        @Path("year") year: Int,
        @Path("month") month: Int
    ): DashboardResponse
}
