package com.tbank.smartbudget.core.network.remote.api

import com.tbank.smartbudget.core.network.remote.dto.CreateGoalRequest
import com.tbank.smartbudget.core.network.remote.dto.GoalContributionRequest
import com.tbank.smartbudget.core.network.remote.dto.GoalDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GoalApi {

    @GET("api/v1/goals/{id}")
    suspend fun get(@Path("id") id: Long): GoalDto

    @PUT("api/v1/goals/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body request: CreateGoalRequest
    ): GoalDto

    @DELETE("api/v1/goals/{id}")
    suspend fun delete(@Path("id") id: Long)

    @POST("api/v1/goals")
    suspend fun create(@Body request: CreateGoalRequest): GoalDto

    @POST("api/v1/goals/{id}/contribute")
    suspend fun contribute(
        @Path("id") id: Long,
        @Body request: GoalContributionRequest
    ): GoalDto

    @GET("api/v1/goals/completed")
    suspend fun listCompleted(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): List<GoalDto>

    @GET("api/v1/goals/active")
    suspend fun listActive(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): List<GoalDto>
}
