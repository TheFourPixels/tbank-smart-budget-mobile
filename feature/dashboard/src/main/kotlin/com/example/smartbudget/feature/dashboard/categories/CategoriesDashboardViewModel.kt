package com.example.smartbudget.feature.dashboard.categories

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.data.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesDashboardViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesDashboardUiState(isLoading = true))
    val uiState: StateFlow<CategoriesDashboardUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Обновленный мок с распределением ровно 100%
            // Общая сумма: 30 000 ₽
            val mockCategories = listOf(
                CategoryDashboardItem(1, "Продукты", "12 000 ₽", 12000.0, Color(0xFF43A047), 0.40f), // 40%
                CategoryDashboardItem(5, "Дом", "6 000 ₽", 6000.0, Color(0xFFFBC02D), 0.20f),      // 20%
                CategoryDashboardItem(2, "Транспорт", "4 500 ₽", 4500.0, Color(0xFF1E88E5), 0.15f), // 15%
                CategoryDashboardItem(3, "Кафе", "4 500 ₽", 4500.0, Color(0xFFFF7043), 0.15f),      // 15%
                CategoryDashboardItem(
                    4,
                    "Одежда",
                    "3 000 ₽",
                    3000.0,
                    Color(0xFF8E24AA),
                    0.10f
                )     // 10%
            ).sortedByDescending { it.amountValue }

            // Мок для графика динамики
            val mockHistory = listOf(
                1200f, 1500f, 800f, 2300f, 4000f, 1200f, 3000f,
                2500f, 1000f, 500f, 4500f, 3200f, 1800f, 2100f
            )

            val total = mockCategories.sumOf { it.amountValue }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    totalSpent = formatMoney(total),
                    categories = mockCategories,
                    historyData = mockHistory
                )
            }
        }
    }

    private fun formatMoney(amount: Double): String {
        return "%,.0f ₽".format(amount).replace(',', ' ')
    }
}