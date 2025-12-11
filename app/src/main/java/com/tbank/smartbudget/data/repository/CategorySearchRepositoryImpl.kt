package com.tbank.smartbudget.data.repository

import android.util.Log
import com.tbank.smartbudget.data.remote.api.BudgetApi
import com.tbank.smartbudget.domain.model.BudgetCategory
import com.tbank.smartbudget.domain.repository.CategorySearchRepository
import javax.inject.Inject

/**
 * Реальная реализация репозитория, использующая API.
 */
class CategorySearchRepositoryImpl @Inject constructor(
    private val api: BudgetApi
) : CategorySearchRepository {

    override suspend fun getAllCategories(): List<BudgetCategory> {
        return try {
            val response = api.getAllCategories()
            // Маппим DTO в доменную модель
            response.content.map { it.toDomain() }
        } catch (e: Exception) {
            // В случае ошибки (нет сети, сервер не запущен) логируем и возвращаем пустой список
            // В реальном приложении здесь стоит возвращать Result.failure
            Log.e("CategoryRepo", "Error fetching categories", e)
            emptyList()
        }
    }
}