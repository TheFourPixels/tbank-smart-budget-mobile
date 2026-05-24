package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.core.network.di.IoDispatcher
import com.tbank.smartbudget.core.network.remote.AppError
import com.tbank.smartbudget.core.network.remote.AppResult
import com.tbank.smartbudget.core.network.remote.api.BudgetApi
import com.tbank.smartbudget.core.network.remote.api.DashboardApi
import com.tbank.smartbudget.core.network.remote.dto.LimitRequestDto
import com.tbank.smartbudget.core.network.remote.dto.SaveBudgetRequest
import com.tbank.smartbudget.core.network.remote.interceptor.ApiException
import com.tbank.smartbudget.core.network.remote.safeApiCall
import com.tbank.smartbudget.data.domain.model.*
import com.tbank.smartbudget.data.domain.repository.BudgetRepository
import com.tbank.smartbudget.data.domain.repository.CategorySearchRepository
import com.tbank.smartbudget.data.repository.mappers.toDomain
import com.tbank.smartbudget.data.repository.mappers.toSummary
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetApi: BudgetApi,
    private val dashboardApi: DashboardApi,
    private val categorySearchRepository: CategorySearchRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BudgetRepository {

    override suspend fun getBudgetDetails(year: Int, month: Int): Result<BudgetDetails> =
        withContext(ioDispatcher) {
            safeApiCall {
                budgetApi.getBudget(year, month).toDomain()
            }.toResult()
        }

    override suspend fun getActiveBudgetSummary(year: Int, month: Int): Result<BudgetSummary> =
        withContext(ioDispatcher) {
            safeApiCall {
                dashboardApi.get(year, month).toSummary()
            }.toResult()
        }

    override suspend fun getCategoryStats(year: Int, month: Int): Result<List<CategoryLimit>> =
        withContext(ioDispatcher) {
            safeApiCall {
                dashboardApi.get(year, month).categoryStats?.map { it.toDomain() } ?: emptyList()
            }.toResult()
        }

    override suspend fun getCategoryLimits(budgetId: Long): Result<List<CategoryLimit>> =
        withContext(ioDispatcher) {
            safeApiCall {
                val now = LocalDate.now()
                val dto = dashboardApi.get(month = now.monthValue, year = now.year)
                dto.categoryStats?.map { it.toDomain() } ?: emptyList()
            }.toResult()
        }

    override suspend fun saveBudget(
        year: Int,
        month: Int,
        totalIncome: Double,
        period: String,
        limits: List<BudgetLimitData>
    ): Result<Unit> = withContext(ioDispatcher) {
        safeApiCall {
            val requestLimits = limits.map {
                LimitRequestDto(
                    categoryId = it.categoryId,
                    limitValue = it.limitValue,
                    limitType = it.limitType.name
                )
            }
            val request = SaveBudgetRequest(year, month, totalIncome, requestLimits)
            budgetApi.saveBudget(request)
            Unit
        }.toResult()
    }

    override suspend fun deleteBudget(year: Int, month: Int): Result<Unit> =
        withContext(ioDispatcher) {
            safeApiCall {
                budgetApi.deleteBudget(year, month)
                Unit
            }.toResult()
        }

    override suspend fun getAllAvailableCategories(): Result<List<BudgetCategory>> = withContext(ioDispatcher) {
        try {
            Result.success(categorySearchRepository.getAllCategories())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createCustomCategory(name: String, iconRes: Int, color: Long): Result<BudgetCategory> =
        Result.failure(UnsupportedOperationException("Use CategoryRepository for this"))

    override suspend fun addCategoryToBudget(year: Int, month: Int, categoryId: Long): Result<Unit> = withContext(ioDispatcher) {
        val currentBudget = getBudgetDetails(year, month).getOrNull()
        if (currentBudget == null) {
             saveBudget(year, month, 0.0, "1 мес", listOf(BudgetLimitData(categoryId, 0.0, BudgetLimitType.SUM)))
        } else {
            val alreadyExists = currentBudget.limits.any { it.categoryId.value == categoryId }
            if (alreadyExists) return@withContext Result.success(Unit)
            
            val newLimits = currentBudget.limits.map { 
                BudgetLimitData(it.categoryId.value, it.limitValue, it.limitType) 
            } + BudgetLimitData(categoryId, 0.0, BudgetLimitType.SUM)
            
            saveBudget(year, month, currentBudget.totalIncome, currentBudget.period, newLimits)
        }
    }

    override suspend fun removeCategoryFromBudget(year: Int, month: Int, categoryId: Long): Result<Unit> = withContext(ioDispatcher) {
        val currentBudget = getBudgetDetails(year, month).getOrNull() ?: return@withContext Result.success(Unit)
        
        val newLimits = currentBudget.limits
            .filter { it.categoryId.value != categoryId }
            .map { BudgetLimitData(it.categoryId.value, it.limitValue, it.limitType) }
        
        saveBudget(year, month, currentBudget.totalIncome, currentBudget.period, newLimits)
    }

    private fun <T> AppResult<T>.toResult(): Result<T> = when (this) {
        is AppResult.Success -> Result.success(data)
        is AppResult.Error -> {
            val exception = when (val err = error) {
                is AppError.Network -> ApiException(err.code, err.message)
                is AppError.NoInternet -> ApiException(0, "No internet connection")
                is AppError.Unknown -> ApiException(-1, err.message)
            }
            Result.failure(exception)
        }
    }
}
