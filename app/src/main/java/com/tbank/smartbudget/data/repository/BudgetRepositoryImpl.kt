package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.data.remote.api.BudgetApi
import com.tbank.smartbudget.data.remote.dto.LimitRequestDto
import com.tbank.smartbudget.data.remote.dto.SaveBudgetRequest
import com.tbank.smartbudget.domain.model.BudgetCategory
import com.tbank.smartbudget.domain.model.BudgetDetails
import com.tbank.smartbudget.domain.model.BudgetLimitData
import com.tbank.smartbudget.domain.model.BudgetSummary
import com.tbank.smartbudget.domain.model.CategoryLimit
import com.tbank.smartbudget.domain.repository.BudgetRepository
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetApi: BudgetApi,
) : BudgetRepository {

    override suspend fun getBudgetDetails(year: Int, month: Int): Result<BudgetDetails> {
        return try {
            val response = budgetApi.getBudget(year, month)

            val dto = response.body() ?: throw Exception("Пустой ответ от сервера")

            // Маппим лимиты (временно без детальной информации о категории,
            // так как связывание пройдет в UseCase)
            val limits = dto.limits.map { limitDto ->
                // Логика маппинга DTO -> Domain Limit
                // limitDto.toDomain()
                TODO("Маппинг лимитов")
            }

            Result.success(
                BudgetDetails(
                    id = dto.id,
                    year = dto.year,
                    month = dto.month,
                    totalIncome = dto.totalIncome,
                    period = "2 мес",
                    limits = limits
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveBudget(
        year: Int,
        month: Int,
        totalIncome: Double,
        period: String,
        limits: List<BudgetLimitData>
    ): Result<Unit> {
        return try {
            val requestLimits =
                limits.map { LimitRequestDto(it.categoryId, it.limitValue, it.limitType) }
            val request = SaveBudgetRequest(year, month, totalIncome, requestLimits)
            val response = budgetApi.saveBudget(request)
            if (response.isSuccessful) Result.success(Unit) else Result.failure(Exception("Ошибка: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBudget(year: Int, month: Int): Result<Unit> {
        return try {
            val response = budgetApi.deleteBudget(year, month)
            if (response.isSuccessful) Result.success(Unit) else Result.failure(Exception("Ошибка: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getActiveBudgetSummary(year: Int, month: Int): Result<BudgetSummary> {
        return try {
            val response = budgetApi.getBudgetDashboard(year, month)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val budgetPlan = dto.totalSpent + dto.remainingBudget
                Result.success(
                    BudgetSummary(
                        dto.totalIncome,
                        budgetPlan,
                        dto.totalSpent,
                        dto.remainingBudget,
                        "Месяц"
                    )
                )
            } else Result.failure(Exception("Ошибка"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCategoryLimits(budgetId: Long): Result<List<CategoryLimit>> {
        return Result.failure(Exception("Not implemented yet"))
    }

    override suspend fun getAllAvailableCategories(): Result<List<BudgetCategory>> {
        return Result.failure(Exception("Метод устарел. Используйте CategorySearchRepository на слое UseCase."))
    }

    override suspend fun createCustomCategory(
        name: String,
        iconRes: Int,
        color: Long
    ): Result<BudgetCategory> {
        return Result.failure(Exception("Метод устарел. Используйте CategorySearchRepository на слое UseCase."))
    }

    override suspend fun addCategoryToBudget(
        year: Int,
        month: Int,
        categoryId: Long
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun removeCategoryFromBudget(
        year: Int,
        month: Int,
        categoryId: Long
    ): Result<Unit> {
        return Result.success(Unit)
    }
}