package com.tbank.smartbudget.feature.budget_tab

import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.datastore.SessionManager
import com.tbank.smartbudget.core.network.di.IoDispatcher
import com.tbank.smartbudget.core.network.remote.interceptor.ApiException
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.data.domain.model.CategoryColorMapper
import com.tbank.smartbudget.data.domain.model.BudgetLimitType
import com.tbank.smartbudget.data.domain.usecase.GetBudgetDetailsUseCase
import com.tbank.smartbudget.data.domain.usecase.GetBudgetSummaryUseCase
import com.tbank.smartbudget.data.domain.usecase.GetCategoryDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val getBudgetSummaryUseCase: GetBudgetSummaryUseCase,
    private val getBudgetDetailsUseCase: GetBudgetDetailsUseCase,
    private val getCategoryDetailsUseCase: GetCategoryDetailsUseCase,
    private val sessionManager: SessionManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<BudgetUiState, BudgetIntent, BudgetEffect>(
    BudgetUiState(isLoading = true)
) {

    init {
        onIntent(BudgetIntent.LoadData)
    }

    override fun onIntent(intent: BudgetIntent) {
        when (intent) {
            BudgetIntent.LoadData, BudgetIntent.OnRefresh -> loadData()
            BudgetIntent.OnBudgetClick -> sendEffect(BudgetEffect.NavigateToBudgetEdit)
            BudgetIntent.OnSearchClick -> sendEffect(BudgetEffect.NavigateToSearch)
            BudgetIntent.OnProfileClick -> sendEffect(BudgetEffect.NavigateToProfile)
            BudgetIntent.OnAllOperationsClick -> sendEffect(BudgetEffect.NavigateToAllOperations)
            BudgetIntent.OnSelectedCategoriesClick -> sendEffect(BudgetEffect.NavigateToSelectedCategories)
        }
    }

    private fun loadData() {
        viewModelScope.launch(ioDispatcher) {
            val savedName = sessionManager.getUserName() ?: "Пользователь"
            val now = LocalDate.now()

            updateState { copy(isLoading = true, error = null, userName = savedName) }

            val summaryDeferred = async { getBudgetSummaryUseCase.execute(year = now.year, month = now.monthValue) }
            val budgetDetailsDeferred = async { getBudgetDetailsUseCase.invoke(year = now.year, month = now.monthValue) }
            val statsDeferred = async { getCategoryDetailsUseCase.execute(budgetId = 0L) }

            val summaryResult = summaryDeferred.await()
            val budgetDetailsResult = budgetDetailsDeferred.await()
            val statsResult = statsDeferred.await()

            if (summaryResult.isSuccess || budgetDetailsResult.isSuccess) {
                val summary = summaryResult.getOrNull()
                val budgetDetails = budgetDetailsResult.getOrNull()
                val stats = statsResult.getOrNull() ?: emptyList()

                android.util.Log.d("BudgetViewModel", "Data loaded. Summary: ${summary?.totalSpent}, Limits: ${budgetDetails?.limits?.size}, Stats: ${stats.size}")
                stats.forEach { android.util.Log.d("BudgetViewModel", "   - Stat: ${it.name} ID=${it.id.value} Spent=${it.spentAmount}") }

                val enrichedCategories = budgetDetails?.limits?.map { limit ->
                    val stat = stats.find { it.id.value == limit.categoryId.value }
                    val limitValue = if (limit.limitType == BudgetLimitType.PERCENT) {
                        (budgetDetails.totalIncome * (limit.limitValue / 100.0))
                    } else {
                        limit.limitValue
                    }
                    
                    if (stat != null) {
                        android.util.Log.d("BudgetViewModel", "Enriching ${limit.categoryName}: found stat with spent ${stat.spentAmount}")
                    }

                    CategoryUi(
                        id = limit.categoryId,
                        name = limit.categoryName,
                        iconRes = limit.iconRes,
                        color = limit.color,
                        spentValue = formatMoney(stat?.spentAmount ?: 0.0),
                        limitValue = formatMoney(limitValue),
                        progress = if (limitValue > 0)
                            ((stat?.spentAmount ?: 0.0) / limitValue).toFloat().coerceIn(0f, 1f)
                        else 0f
                    )
                } ?: emptyList()

                updateState {
                    copy(
                        budgetTerm = summary?.period ?: "",
                        hasBudget = budgetDetails != null,
                        summary = if (summary != null) BudgetSummaryUi(
                            totalIncome = formatMoney(summary.totalIncome),
                            totalLimit = formatMoney(summary.totalLimit),
                            totalSpent = formatMoney(summary.totalSpent),
                            freeFunds = formatMoney(summary.freeFunds),
                            progress = if (summary.totalLimit > 0)
                                (summary.totalSpent / summary.totalLimit).toFloat().coerceIn(0f, 1f)
                            else 0f
                        ) else null,
                        categories = enrichedCategories,
                        isLoading = false
                    )
                }
            } else {
                val error = summaryResult.exceptionOrNull()
                if (error is ApiException && error.code == 404) {
                    updateState { 
                        copy(
                            isLoading = false, 
                            hasBudget = false,
                            summary = null,
                            categories = emptyList()
                        ) 
                    }
                } else {
                    updateState { copy(isLoading = false, error = "Ошибка обновления данных") }
                }
            }
        }
    }

    private fun formatMoney(amount: Double): String {
        return "%,.0f ₽".format(amount).replace(',', ' ')
    }
}
