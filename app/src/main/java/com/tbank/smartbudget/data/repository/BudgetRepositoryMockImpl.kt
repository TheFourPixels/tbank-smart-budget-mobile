package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.domain.model.*
import com.tbank.smartbudget.domain.repository.BudgetRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.collections.map

/**
 * Заглушка (Mock) для BudgetRepository с состоянием в памяти.
 */
class BudgetRepositoryMockImpl @Inject constructor() : BudgetRepository {

    // Имитация базы данных в памяти
    private var currentIncome = 100000.0

    // Храним лимиты в памяти, чтобы saveBudget обновлял их
    private var currentLimits = mutableListOf(
        BudgetLimitModel(1, "Продукты", 30.0, BudgetLimitType.PERCENT, 0, 0xFF43A047),
        BudgetLimitModel(2, "Транспорт", 10.0, BudgetLimitType.PERCENT, 0, 0xFFFBC02D),
        BudgetLimitModel(3, "Маркетплейсы", 5000.0, BudgetLimitType.AMOUNT, 0, 0xFF1E88E5)
    )

    override suspend fun getBudgetDetails(year: Int, month: Int): Result<BudgetDetails> {
        delay(500)
        // Возвращаем текущее состояние "БД"
        return Result.success(
            BudgetDetails(
                id = 1,
                year = year,
                month = month,
                totalIncome = currentIncome,
                limits = currentLimits.toList() // Возвращаем копию списка
            )
        )
    }

    override suspend fun saveBudget(
        year: Int, month: Int, totalIncome: Double, limits: List<BudgetLimitData>
    ): Result<Unit> {
        delay(1000)
        // "Сохраняем" данные в нашу переменную
        currentIncome = totalIncome

        // Обновляем список лимитов. В реальности мы бы сохраняли в БД.
        // Здесь мы просто маппим новые данные обратно в BudgetLimitModel для мока,
        // сохраняя имена и цвета (в реальном репо это делал бы JOIN с таблицей категорий)
        val updatedLimits = limits.map { newLimit ->
            val existing = currentLimits.find { it.categoryId == newLimit.categoryId }
            BudgetLimitModel(
                categoryId = newLimit.categoryId,
                categoryName = existing?.categoryName ?: "Категория ${newLimit.categoryId}", // Fallback имя
                limitValue = newLimit.limitValue,
                limitType = if (newLimit.limitType == "PERCENT") BudgetLimitType.PERCENT else BudgetLimitType.AMOUNT,
                iconRes = existing?.iconRes ?: 0,
                color = existing?.color ?: 0xFF9E9E9E
            )
        }
        currentLimits = updatedLimits.toMutableList()

        return Result.success(Unit)
    }

    // --- Остальные методы (адаптированные) ---

    override suspend fun getActiveBudgetSummary(year: Int, month: Int): Result<BudgetSummary> {
        delay(500)
        // Считаем тотал лимитов для саммари
        val totalLimitsAmount = currentLimits.sumOf {
            if (it.limitType == BudgetLimitType.AMOUNT) it.limitValue
            else currentIncome * (it.limitValue / 100)
        }

        val summary = BudgetSummary(
            totalIncome = currentIncome,
            totalLimit = totalLimitsAmount,
            totalSpent = totalLimitsAmount * 0.4, // Фейковый расход 40%
            freeFunds = currentIncome - totalLimitsAmount
        )
        return Result.success(summary)
    }

    override suspend fun getCategoryLimits(budgetId: Long): Result<List<CategoryLimit>> {
        delay(500)
        // Преобразуем наши BudgetLimitModel в CategoryLimit для экрана просмотра
        val list = currentLimits.map {
            val limitRub = if (it.limitType == BudgetLimitType.AMOUNT) it.limitValue else currentIncome * (it.limitValue / 100)
            CategoryLimit(
                id = it.categoryId,
                name = it.categoryName,
                limitAmount = limitRub,
                spentAmount = limitRub * 0.3, // Фейковый расход
                iconRes = it.iconRes,
                color = it.color
            )
        }
        return Result.success(list)
    }

    override suspend fun getAllAvailableCategories(): Result<List<BudgetCategory>> {
        return Result.success(emptyList())
    }

    override suspend fun createCustomCategory(name: String, iconRes: Int, color: Long): Result<BudgetCategory> {
        return Result.failure(Exception("Not implemented"))
    }
}