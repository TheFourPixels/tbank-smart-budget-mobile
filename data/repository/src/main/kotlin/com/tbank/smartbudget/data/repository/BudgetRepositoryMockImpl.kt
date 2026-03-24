package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.data.domain.model.BudgetCategory
import com.tbank.smartbudget.data.domain.model.BudgetDetails
import com.tbank.smartbudget.data.domain.model.BudgetLimitData
import com.tbank.smartbudget.data.domain.model.BudgetLimitModel
import com.tbank.smartbudget.data.domain.model.BudgetLimitType
import com.tbank.smartbudget.data.domain.model.BudgetSummary
import com.tbank.smartbudget.data.domain.model.CategoryLimit
import com.tbank.smartbudget.data.domain.repository.BudgetRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.collections.map

/**
 * Заглушка (Mock) для BudgetRepository с состоянием в памяти.
 */
class BudgetRepositoryMockImpl @Inject constructor() : BudgetRepository {

    private var currentIncome = 100000.0
    private var currentPeriod = "2 мес"

    // Текущие лимиты (Selected Categories)
    private var currentLimits = mutableListOf(
        BudgetLimitModel(1, "Продукты", 30.0, BudgetLimitType.PERCENT, 0, 0xFF43A047),
        BudgetLimitModel(2, "Транспорт", 10.0, BudgetLimitType.PERCENT, 0, 0xFFFBC02D),
        BudgetLimitModel(3, "Маркетплейсы", 5000.0, BudgetLimitType.SUM, 0, 0xFF1E88E5)
    )

    // Справочник всех категорий
    private val allCategoriesDb = listOf(
        BudgetCategory(1, "Продукты", 0, 0xFF43A047),
        BudgetCategory(2, "Транспорт", 0, 0xFFFBC02D),
        BudgetCategory(3, "Маркетплейсы", 0, 0xFF1E88E5),
        BudgetCategory(4, "Кафе и рестораны", 0, 0xFFEF5350),
        BudgetCategory(5, "Развлечения", 0, 0xFFAB47BC),
        BudgetCategory(6, "Здоровье", 0, 0xFF26A69A),
        BudgetCategory(7, "Образование", 0, 0xFF5C6BC0),
        BudgetCategory(8, "Одежда", 0, 0xFFFF7043)
    )

    override suspend fun getBudgetDetails(year: Int, month: Int): Result<BudgetDetails> {
        delay(300)
        // Имитируем, что бюджет найден (200 OK)
        return Result.success(
            BudgetDetails(
                id = 1,
                year = year,
                month = month,
                totalIncome = currentIncome,
                period = currentPeriod,
                limits = currentLimits.toList()
            )
        )
    }

    override suspend fun saveBudget(
        year: Int, month: Int, totalIncome: Double, period: String, limits: List<BudgetLimitData>
    ): Result<Unit> {
        delay(500)
        currentIncome = totalIncome
        currentPeriod = period

        val updatedLimits = limits.map { newLimit ->
            val catInfo = allCategoriesDb.find { it.id == newLimit.categoryId }
            BudgetLimitModel(
                categoryId = newLimit.categoryId,
                categoryName = catInfo?.name ?: "Категория ${newLimit.categoryId}",
                limitValue = newLimit.limitValue,
                limitType = if (newLimit.limitType == "PERCENT") BudgetLimitType.PERCENT else BudgetLimitType.SUM,
                iconRes = catInfo?.iconRes ?: 0,
                color = catInfo?.color ?: 0xFF9E9E9E
            )
        }
        currentLimits = updatedLimits.toMutableList()
        return Result.success(Unit)
    }

    override suspend fun deleteBudget(year: Int, month: Int): Result<Unit> {
        delay(500)
        // Имитируем удаление: очищаем лимиты и сбрасываем доход
        currentIncome = 0.0
        currentLimits.clear()
        return Result.success(Unit)
    }

    override suspend fun getActiveBudgetSummary(year: Int, month: Int): Result<BudgetSummary> {
        delay(300)
        val totalLimitsSUM = currentLimits.sumOf {
            if (it.limitType == BudgetLimitType.SUM) it.limitValue
            else currentIncome * (it.limitValue / 100)
        }
        return Result.success(
            BudgetSummary(
                totalIncome = currentIncome,
                totalLimit = totalLimitsSUM,
                totalSpent = totalLimitsSUM * 0.4,
                freeFunds = currentIncome - totalLimitsSUM,
                period = currentPeriod
            )
        )
    }

    override suspend fun getCategoryLimits(budgetId: Long): Result<List<CategoryLimit>> {
        delay(300)
        return Result.success(currentLimits.map {
            val limitRub = if (it.limitType == BudgetLimitType.SUM) it.limitValue else currentIncome * (it.limitValue / 100)
            CategoryLimit(
                it.categoryId,
                it.categoryName,
                limitRub,
                limitRub * 0.3,
                it.iconRes,
                it.color
            )
        })
    }

    override suspend fun getAllAvailableCategories(): Result<List<BudgetCategory>> {
        delay(300)
        return Result.success(allCategoriesDb)
    }

    override suspend fun createCustomCategory(name: String, iconRes: Int, color: Long): Result<BudgetCategory> {
        return Result.failure(Exception("Not implemented"))
    }

    override suspend fun addCategoryToBudget(year: Int, month: Int, categoryId: Long): Result<Unit> {
        if (currentLimits.any { it.categoryId == categoryId }) return Result.success(Unit)
        val catInfo = allCategoriesDb.find { it.id == categoryId } ?: return Result.failure(Exception("Категория не найдена"))
        currentLimits.add(
            BudgetLimitModel(
                catInfo.id,
                catInfo.name,
                0.0,
                BudgetLimitType.SUM,
                catInfo.iconRes,
                catInfo.color
            )
        )
        return Result.success(Unit)
    }

    override suspend fun removeCategoryFromBudget(year: Int, month: Int, categoryId: Long): Result<Unit> {
        currentLimits.removeAll { it.categoryId == categoryId }
        return Result.success(Unit)
    }
}