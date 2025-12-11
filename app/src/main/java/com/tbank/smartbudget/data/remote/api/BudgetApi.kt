package com.tbank.smartbudget.data.remote.api

import com.tbank.smartbudget.data.remote.dto.CategoryResponseDto
import retrofit2.http.GET

interface BudgetApi {

    // Получить список всех доступных категорий
    @GET("categories")
    suspend fun getAllCategories(): CategoryResponseDto

    // Здесь позже будут методы для POST /budgets и т.д.
}