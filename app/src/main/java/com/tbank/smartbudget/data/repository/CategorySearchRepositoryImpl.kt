package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.data.remote.api.CategoryApi
import com.tbank.smartbudget.domain.model.BudgetCategory
import com.tbank.smartbudget.domain.repository.CategorySearchRepository
import javax.inject.Inject

class CategorySearchRepositoryImpl @Inject constructor(
    private val api: CategoryApi
) : CategorySearchRepository {

    override suspend fun getAllCategories(): List<BudgetCategory> {
        return try {
            // Запрашиваем все категории (страница 0, размер 100)
            val response = api.getCategories(page = 0, size = 100)
            if (response.isSuccessful && response.body() != null) {
                val dtos = response.body()!!.content
                dtos.map { dto ->
                    BudgetCategory(
                        id = dto.id,
                        name = dto.name,
                        iconRes = 0,
                        color = generateColor(dto.id)
                    )
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun generateColor(id: Long): Long {
        val colors = listOf(
            0xFFEF5350, 0xFFEC407A, 0xFFAB47BC, 0xFF7E57C2, 0xFF5C6BC0,
            0xFF42A5F5, 0xFF29B6F6, 0xFF26C6DA, 0xFF26A69A, 0xFF66BB6A,
            0xFF9CCC65, 0xFFD4E157, 0xFFFFEE58, 0xFFFFCA28, 0xFFFFA726,
            0xFFFF7043, 0xFF8D6E63, 0xFFBDBDBD, 0xFF78909C
        )
        return colors[(id % colors.size).toInt()]
    }
}