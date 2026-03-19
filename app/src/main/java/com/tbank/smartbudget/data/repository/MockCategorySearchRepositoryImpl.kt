package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.domain.model.BudgetCategory
import com.tbank.smartbudget.domain.repository.CategorySearchRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockCategorySearchRepositoryImpl @Inject constructor() : CategorySearchRepository {

    private val allCategories = listOf(
        BudgetCategory(1, "Продукты", 0, 0xFF43A047),
        BudgetCategory(2, "Транспорт", 0, 0xFFFBC02D),
        BudgetCategory(3, "Маркетплейсы", 0, 0xFF1E88E5),
        BudgetCategory(4, "Кафе и рестораны", 0, 0xFFEF5350),
        BudgetCategory(5, "Развлечения", 0, 0xFFAB47BC),
        BudgetCategory(6, "Здоровье", 0, 0xFF26A69A),
        BudgetCategory(7, "Образование", 0, 0xFF5C6BC0),
        BudgetCategory(8, "Одежда", 0, 0xFFFF7043)
    )

    override suspend fun getAllCategories(): List<BudgetCategory> {
        delay(300)
        return allCategories
    }
}