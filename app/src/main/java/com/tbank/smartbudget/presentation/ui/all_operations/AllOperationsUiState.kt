package com.tbank.smartbudget.presentation.ui.all_operations

import androidx.compose.ui.graphics.Color
import java.time.LocalDate

data class AllOperationsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    // Строка для отображения диапазона (например "16 окт - 22 окт")
    val dateRangeLabel: String = "",

    // Выбранный месяц (для заголовка, если нужно, или убираем если заменяем календарем)
    val selectedMonth: String = "",

    // Убираем selectedFilter, так как вкладку "Все" удаляем
    // val selectedFilter: String = "Все",

    val totalExpense: String = "0 ₽",
    val periodType: PeriodType = PeriodType.MONTH,

    val selectedCategoryNames: Set<String> = emptySet(),

    val chartData: List<ChartDataUi> = emptyList(),
    val transactionGroups: List<TransactionGroupUi> = emptyList()
)

data class ChartDataUi(
    val categoryName: String,
    val amount: String,
    val color: Color,
    val percentage: Float
)

enum class PeriodType {
    WEEK, MONTH, CUSTOM // Добавили CUSTOM для выбора через календарь
}

data class TransactionGroupUi(
    val dateHeader: String,
    val dayTotal: String,
    val items: List<TransactionUi>
)

data class TransactionUi(
    val id: Long,
    val title: String,
    val subtitle: String,
    val amount: String,
    val amountColor: Color,
    val iconColor: Color
)