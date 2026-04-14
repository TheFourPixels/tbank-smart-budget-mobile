package com.example.smartbudget.feature.dashboard.categories

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.core.ui.common.CategoryColorMapper
import com.tbank.smartbudget.data.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesDashboardViewModel @Inject constructor(
    private val repository: BudgetRepository
) : BaseViewModel<CategoriesDashboardUiState, CategoriesDashboardIntent, CategoriesDashboardEffect>(
    CategoriesDashboardUiState(isLoading = true)
) {

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

            val budgetResult = repository.getActiveBudgetSummary(2025, 12)

            val ids = listOf(1L, 5L, 2L, 3L, 4L)
            val mockCategories = ids.map { id ->
                val amount = (1000..5000).random().toDouble()
                CategoryDashboardItem(
                    id = id,
                    name = "Категория $id",
                    amountStr = formatMoney(amount),
                    amountValue = amount,
                    color = Color(CategoryColorMapper.getColorForId(id)),
                    percent = 0.2f
                )
            }.sortedByDescending { it.amountValue }

            val total = mockCategories.sumOf { it.amountValue }

            updateState {
                copy(
                    isLoading = false,
                    totalSpent = formatMoney(total),
                    categories = mockCategories,
                    historyData = listOf(1200f, 1500f, 800f, 2300f, 4000f)
                )
            }
        }
    }

    private fun formatMoney(amount: Double): String = "%,.0f ₽".format(amount).replace(',', ' ')
}