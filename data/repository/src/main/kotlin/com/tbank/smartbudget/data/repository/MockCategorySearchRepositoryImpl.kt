package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.data.domain.model.BudgetCategory
import com.tbank.smartbudget.data.domain.model.CategoryColorMapper
import com.tbank.smartbudget.data.domain.model.CategoryId
import com.tbank.smartbudget.data.domain.repository.CategorySearchRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockCategorySearchRepositoryImpl @Inject constructor() : CategorySearchRepository {

    private val allCategories = listOf(
        BudgetCategory(CategoryId(1), "Продукты", 0, CategoryColorMapper.getColorForId(1)),
        BudgetCategory(CategoryId(2), "Транспорт", 0, CategoryColorMapper.getColorForId(2)),
        BudgetCategory(CategoryId(3), "Маркетплейсы", 0, CategoryColorMapper.getColorForId(3)),
        BudgetCategory(CategoryId(4), "Кафе и рестораны", 0, CategoryColorMapper.getColorForId(4)),
        BudgetCategory(CategoryId(5), "Развлечения", 0, CategoryColorMapper.getColorForId(5)),
        BudgetCategory(CategoryId(6), "Здоровье", 0, CategoryColorMapper.getColorForId(6)),
        BudgetCategory(CategoryId(7), "Образование", 0, CategoryColorMapper.getColorForId(7)),
        BudgetCategory(CategoryId(8), "Одежда", 0, CategoryColorMapper.getColorForId(8))
    )

    override suspend fun getAllCategories(): List<BudgetCategory> {
        delay(300)
        return allCategories
    }

    override suspend fun createCategory(name: String): Result<Long> {
        return Result.success(999L)
    }
}
