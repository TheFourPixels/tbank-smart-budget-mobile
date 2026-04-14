package com.example.smartbudget.feature.operations

import androidx.compose.ui.graphics.Color
import com.tbank.smartbudget.core.ui.common.UiState
import com.tbank.smartbudget.data.domain.model.TransactionId

data class AllOperationsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val dateRangeLabel: String = "",
    val totalExpense: String = "0 ₽",
    val periodType: PeriodType = PeriodType.MONTH,
    val selectedCategoryNames: Set<String> = emptySet(),
    val chartData: List<ChartDataUi> = emptyList(),
    val transactionGroups: List<TransactionGroupUi> = emptyList()
) : UiState

data class ChartDataUi(
    val categoryName: String,
    val amount: String,
    val color: Color,
    val percentage: Float
)

enum class PeriodType {
    WEEK, MONTH, CUSTOM
}

data class TransactionGroupUi(
    val dateHeader: String,
    val dayTotal: String,
    val items: List<TransactionUi>
)

data class TransactionUi(
    val id: TransactionId,
    val title: String,
    val subtitle: String,
    val amount: String,
    val amountColor: Color,
    val iconColor: Color
)