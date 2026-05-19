package com.example.smartbudget.feature.dashboard.categories

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.data.domain.model.TransactionType
import com.tbank.smartbudget.data.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CategoriesDashboardViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : BaseViewModel<CategoriesDashboardUiState, CategoriesDashboardIntent, CategoriesDashboardEffect>(
    CategoriesDashboardUiState(isLoading = true)
) {

    private val now = LocalDate.now()
    private val currentYear = now.year
    private val currentMonth = now.monthValue

    init {
        onIntent(CategoriesDashboardIntent.LoadData)
    }

    override fun onIntent(intent: CategoriesDashboardIntent) {
        when (intent) {
            CategoriesDashboardIntent.LoadData -> loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val startOfMonth = LocalDate.of(currentYear, currentMonth, 1).atStartOfDay()
            val endOfMonth = LocalDate.of(currentYear, currentMonth, 1).plusMonths(1).atStartOfDay().minusNanos(1)

            // We fetch transactions and aggregate them locally to ensure categorization rules are applied
            transactionRepository.getTransactions(
                page = 0,
                size = 500,
                startDate = startOfMonth,
                endDate = endOfMonth
            ).onSuccess { transactions ->
                val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
                val total = expenses.sumOf { it.amount }

                val uiCategories = expenses
                    .groupBy { it.categoryName }
                    .map { (name, list) ->
                        val amount = list.sumOf { it.amount }
                        CategoryDashboardItem(
                            id = list.first().categoryId.value,
                            name = name,
                            amountStr = formatMoney(amount),
                            amountValue = amount,
                            color = Color(list.first().categoryColor),
                            percent = if (total > 0) (amount / total).toFloat() else 0f
                        )
                    }.sortedByDescending { it.amountValue }

                updateState {
                    copy(
                        isLoading = false,
                        totalSpent = formatMoney(total),
                        categories = uiCategories,
                        historyData = emptyList()
                    )
                }
            }.onFailure {
                updateState { copy(isLoading = false) }
            }
        }
    }

    private fun formatMoney(amount: Double): String = "%,.0f ₽".format(amount).replace(',', ' ')
}