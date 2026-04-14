package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.data.domain.model.BudgetCategory
import com.tbank.smartbudget.data.domain.model.CategoryId
import com.tbank.smartbudget.data.domain.repository.CategorySearchRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockCategorySearchRepositoryImpl @Inject constructor() : CategorySearchRepository {

    private val allCategories = listOf(
        BudgetCategory(CategoryId(1), "Продукты", 0, 0xFF43A047),
        BudgetCategory(CategoryId(2), "Транспорт", 0, 0xFFFBC02D),
        BudgetCategory(CategoryId(3), "Маркетплейсы", 0, 0xFF1E88E5),
        BudgetCategory(CategoryId(4), "Кафе и рестораны", 0, 0xFFEF5350),
        BudgetCategory(CategoryId(5), "Развлечения", 0, 0xFFAB47BC),
        BudgetCategory(CategoryId(6), "Здоровье", 0, 0xFF26A69A),
        BudgetCategory(CategoryId(7), "Образование", 0, 0xFF5C6BC0),
        BudgetCategory(CategoryId(8), "Одежда", 0, 0xFFFF7043)
    )

    override suspend fun getAllCategories(): List<BudgetCategory> {
        delay(300)
        return allCategories
    }
}