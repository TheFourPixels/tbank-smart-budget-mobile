package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.data.remote.api.BudgetApi
import com.tbank.smartbudget.data.remote.api.CategoryApi
import com.tbank.smartbudget.data.remote.dto.CreateCategoryRequest
import com.tbank.smartbudget.data.remote.dto.LimitRequestDto
import com.tbank.smartbudget.data.remote.dto.SaveBudgetRequest
import com.tbank.smartbudget.domain.model.BudgetCategory
import com.tbank.smartbudget.domain.model.BudgetDetails
import com.tbank.smartbudget.domain.model.BudgetLimitData
import com.tbank.smartbudget.domain.model.BudgetLimitModel
import com.tbank.smartbudget.domain.model.BudgetLimitType
import com.tbank.smartbudget.domain.model.BudgetSummary
import com.tbank.smartbudget.domain.model.CategoryLimit
import com.tbank.smartbudget.domain.repository.BudgetRepository
import com.tbank.smartbudget.domain.repository.CategorySearchRepository
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetApi: BudgetApi,
    private val categoryApi: CategoryApi,
    private val categoryRepository: CategorySearchRepository
) : BudgetRepository {

    override suspend fun getBudgetDetails(year: Int, month: Int): Result<BudgetDetails> {
        return try {
            val response = budgetApi.getBudget(year, month)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!

                val allCategories = categoryRepository.getAllCategories()

                val limits = dto.limits.map { limitDto ->
                    val category = allCategories.find { it.id == limitDto.categoryId }
                    BudgetLimitModel(
                        categoryId = limitDto.categoryId,
                        categoryName = category?.name ?: "Категория ${limitDto.categoryId}",
                        limitValue = limitDto.limitValue,
                        limitType = if (limitDto.limitType == "PERCENT") BudgetLimitType.PERCENT else BudgetLimitType.AMOUNT,
                        iconRes = category?.iconRes ?: 0,
                        color = category?.color ?: 0xFF9E9E9E
                    )
                }
                Result.success(BudgetDetails(dto.id, dto.year, dto.month, dto.totalIncome, "2 мес", limits))
            } else {
                if (response.code() == 404) Result.failure(Exception("Бюджет не найден"))
                else Result.failure(Exception("Ошибка сервера: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveBudget(year: Int, month: Int, totalIncome: Double, period: String, limits: List<BudgetLimitData>): Result<Unit> {
        return try {
            val requestLimits = limits.map { LimitRequestDto(it.categoryId, it.limitValue, it.limitType) }
            val request = SaveBudgetRequest(year, month, totalIncome, requestLimits)
            val response = budgetApi.saveBudget(request)
            if (response.isSuccessful) Result.success(Unit) else Result.failure(Exception("Ошибка: ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun deleteBudget(year: Int, month: Int): Result<Unit> {
        return try {
            val response = budgetApi.deleteBudget(year, month)
            if (response.isSuccessful) Result.success(Unit) else Result.failure(Exception("Ошибка: ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun getActiveBudgetSummary(year: Int, month: Int): Result<BudgetSummary> {
        return try {
            val response = budgetApi.getBudgetDashboard(year, month)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(BudgetSummary(dto.budgetPlan + dto.remainingBudget, dto.budgetPlan, dto.totalSpent, dto.remainingBudget, "Месяц"))
            } else Result.failure(Exception("Ошибка"))
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun getCategoryLimits(budgetId: Long): Result<List<CategoryLimit>> {
        return Result.failure(Exception("Not implemented yet"))
    }

    override suspend fun getAllAvailableCategories(): Result<List<BudgetCategory>> {
        return Result.success(categoryRepository.getAllCategories())
    }

    override suspend fun createCustomCategory(name: String, iconRes: Int, color: Long): Result<BudgetCategory> {
        return try {
            val response = categoryApi.createCategory(CreateCategoryRequest(name))
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(
                    BudgetCategory(
                        id = dto.id,
                        name = dto.name,
                        iconRes = iconRes,
                        color = color
                    )
                )
            } else {
                Result.failure(Exception("Ошибка создания: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addCategoryToBudget(year: Int, month: Int, categoryId: Long): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun removeCategoryFromBudget(year: Int, month: Int, categoryId: Long): Result<Unit> {
        return Result.success(Unit)
    }
}