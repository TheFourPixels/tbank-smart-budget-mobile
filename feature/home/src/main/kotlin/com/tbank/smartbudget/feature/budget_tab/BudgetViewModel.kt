package com.tbank.smartbudget.feature.budget_tab

import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.datastore.SessionManager
import com.tbank.smartbudget.core.network.di.IoDispatcher
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.core.ui.common.CategoryColorMapper
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
            val categoriesDeferred = async { getCategoryDetailsUseCase.execute(budgetId = 0L) }

            val summaryResult = summaryDeferred.await()
            val categoriesResult = categoriesDeferred.await()

            if (summaryResult.isSuccess && categoriesResult.isSuccess) {
                val summary = summaryResult.getOrThrow()
                val domainCategories = categoriesResult.getOrThrow()

                updateState {
                    copy(
                        budgetTerm = summary.period,
                        summary = BudgetSummaryUi(
                            totalIncome = formatMoney(summary.totalIncome),
                            totalLimit = formatMoney(summary.totalLimit),
                            totalSpent = formatMoney(summary.totalSpent),
                            freeFunds = formatMoney(summary.freeFunds)
                        ),
                        categories = domainCategories.map { dc ->
                            CategoryUi(
                                id = dc.id,
                                name = dc.name,
                                iconRes = dc.iconRes,
                                color = CategoryColorMapper.getColorForId(dc.id.value),
                                spentValue = formatMoney(dc.spentAmount),
                                limitValue = formatMoney(dc.limitAmount),
                                progress = if (dc.limitAmount > 0)
                                    (dc.spentAmount / dc.limitAmount).toFloat().coerceIn(0f, 1f)
                                else 0f
                            )
                        },
                        isLoading = false
                    )
                }
            } else {
                updateState { copy(isLoading = false, error = "Ошибка обновления данных") }
            }
        }
    }

    private fun formatMoney(amount: Double): String {
        return "%,.0f ₽".format(amount).replace(',', ' ')
    }
}