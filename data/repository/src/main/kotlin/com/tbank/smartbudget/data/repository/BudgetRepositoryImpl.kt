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
import com.tbank.smartbudget.data.repository.mappers.toDomain
import com.tbank.smartbudget.data.repository.mappers.toSummary
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetApi: BudgetApi,
    private val dashboardApi: DashboardApi,
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
                budgetApi.getBudgetDashboard(year, month).toSummary()
            }.toResult()
        }

    override suspend fun getCategoryLimits(budgetId: Long): Result<List<CategoryLimit>> =
        withContext(ioDispatcher) {
            safeApiCall {
                val now = LocalDate.now()
                val dto = dashboardApi.getDashboardSummary(month = now.monthValue, year = now.year)
                dto.categoryStats.map { it.toDomain() }
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

    override suspend fun getAllAvailableCategories(): Result<List<BudgetCategory>> = Result.success(emptyList())

    override suspend fun createCustomCategory(name: String, iconRes: Int, color: Long): Result<BudgetCategory> =
        Result.failure(UnsupportedOperationException("Use CategoryRepository for this"))

    override suspend fun addCategoryToBudget(year: Int, month: Int, categoryId: Long): Result<Unit> = Result.success(Unit)
    override suspend fun removeCategoryFromBudget(year: Int, month: Int, categoryId: Long): Result<Unit> = Result.success(Unit)

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