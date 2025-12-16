package com.tbank.smartbudget.presentation.ui.all_operations

import androidx.compose.ui.graphics.Color

data class AllOperationsUiState(
    val selectedMonth: String = "Октябрь",
    val selectedFilter: String = "Все",
    val totalAmount: String = "5 878 ₽",
    val periodType: PeriodType = PeriodType.MONTH,

    // Данные для диаграммы и списка
    val categories: List<OperationCategoryUi> = listOf(
        OperationCategoryUi(1, "Сервис", "800 ₽", 0xFF9575CD), // Purple
        OperationCategoryUi(2, "ЖКХ", "100 ₽", 0xFFF06292),    // Pink
        OperationCategoryUi(3, "Переводы", "900 ₽", 0xFF4DD0E1) // Cyan
    ),

    val transactions: List<TransactionGroupUi> = listOf(
        TransactionGroupUi(
            date = "Сегодня",
            totalAmount = "-3 500 ₽",
            items = listOf(
                TransactionUi(
                    id = 1,
                    name = "Парковки России",
                    categoryName = "Транспорт",
                    amount = "-50 ₽",
                    iconColor = 0xFF7CB342 // Light Green
                ),
                TransactionUi(
                    id = 2,
                    name = "Мтс",
                    categoryName = "Связь",
                    amount = "-100 ₽",
                    iconColor = 0xFFE53935 // Red
                )
            )
        )
    )
)

enum class PeriodType {
    WEEK, MONTH
}

data class OperationCategoryUi(
    val id: Long,
    val name: String,
    val amount: String,
    val color: Long
)

data class TransactionGroupUi(
    val date: String,
    val totalAmount: String,
    val items: List<TransactionUi>
)

data class TransactionUi(
    val id: Long,
    val name: String,
    val categoryName: String,
    val amount: String,
    val iconColor: Long
)