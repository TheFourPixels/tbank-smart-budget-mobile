package com.tbank.smartbudget.core.network.remote.api

import com.tbank.smartbudget.core.network.remote.dto.CreateGoalRequest
import com.tbank.smartbudget.core.network.remote.dto.GoalDto
import com.tbank.smartbudget.core.network.remote.dto.UpdateGoalAmountRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GoalApi {

    @GET("goals")
    suspend fun getGoals(): List<GoalDto>

    @POST("goals")
    suspend fun createGoal(@Body request: CreateGoalRequest): GoalDto

    @GET("goals/{id}")
    suspend fun getGoalDetails(@Path("id") id: Long): GoalDto

    @DELETE("goals/{id}")
    suspend fun deleteGoal(@Path("id") id: Long)

    @PUT("goals/{id}/contribute")
    suspend fun contributeToGoal(
        @Path("id") id: Long,
        @Body request: UpdateGoalAmountRequest
    ): GoalDto
}