package com.tbank.smartbudget.data.repository

import android.util.Log
import com.tbank.smartbudget.core.network.remote.api.CategoryApi
import com.tbank.smartbudget.core.network.remote.dto.CreateCategoryRequest
import com.tbank.smartbudget.data.domain.model.BudgetCategory
import com.tbank.smartbudget.data.domain.model.CategoryColorMapper
import com.tbank.smartbudget.data.domain.model.CategoryId
import com.tbank.smartbudget.data.domain.repository.CategorySearchRepository
import javax.inject.Inject

class CategorySearchRepositoryImpl @Inject constructor(
    private val api: CategoryApi
) : CategorySearchRepository {

    override suspend fun getAllCategories(): List<BudgetCategory> {
        val allCategories = mutableListOf<BudgetCategory>()
        var currentPage = 0
        var isLastPage = false

        try {
            while (!isLastPage) {
                Log.d("CategorySearch", "Fetching page $currentPage")
                // Запрашиваем очередную страницу
                val response = api.getCategories(page = currentPage, size = 50)
                if (response.isSuccessful && response.body() != null) {
                    val pageDto = response.body()!!
                    Log.d("CategorySearch", "Received ${pageDto.content.size} items, last=${pageDto.last}")
                    
                    // Маппим DTO в доменную модель и добавляем в общий список
                    val domainItems = pageDto.content.map { dto ->
                        BudgetCategory(
                            id = CategoryId(dto.id ?: 0L),
                            name = dto.name ?: "Без категории",
                            iconRes = 0,
                            color = CategoryColorMapper.getColorForId(dto.id ?: 0L)
                        )
                    }
                    allCategories.addAll(domainItems)
                    
                    // Проверяем, последняя ли это страница
                    isLastPage = pageDto.last
                    currentPage++
                } else {
                    Log.e("CategorySearch", "Error fetching categories: ${response.code()} ${response.errorBody()?.string()}")
                    // Если запрос не удался, прерываем цикл
                    break
                }
            }
        } catch (e: Exception) {
            Log.e("CategorySearch", "Exception fetching categories", e)
            // В случае ошибки возвращаем то, что успели загрузить (или пустой список)
        }

        return allCategories
    }

    override suspend fun createCategory(name: String): Result<Long> {
        return try {
            val response = api.createCategory(CreateCategoryRequest(name))
            val body = response.body()
            if (response.isSuccessful && body != null && body.id != null) {
                Result.success(body.id!!)
            } else {
                Result.failure(Exception("Failed to create category: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
