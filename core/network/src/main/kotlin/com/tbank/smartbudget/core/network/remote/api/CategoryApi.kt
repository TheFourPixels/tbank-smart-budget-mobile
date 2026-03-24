package com.tbank.smartbudget.core.network.remote.api

import com.tbank.smartbudget.core.network.remote.dto.CategoryDto
import com.tbank.smartbudget.core.network.remote.dto.CategoryPagedResponse
import com.tbank.smartbudget.core.network.remote.dto.CreateCategoryRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryApi {

    @GET("api/v1/categories")
    suspend fun getCategories(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 100
    ): Response<CategoryPagedResponse>

    @POST("api/v1/categories")
    suspend fun createCategory(
        @Body request: CreateCategoryRequest
    ): Response<CategoryDto>

    @PUT("api/v1/categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Long,
        @Body request: CreateCategoryRequest
    ): Response<CategoryDto>

    @DELETE("api/v1/categories/{id}")
    suspend fun deleteCategory(
        @Path("id") id: Long
    ): Response<Unit>
}