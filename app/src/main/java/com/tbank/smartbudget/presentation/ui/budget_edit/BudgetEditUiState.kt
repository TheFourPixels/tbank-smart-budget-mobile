package com.tbank.smartbudget.presentation.ui.budget_edit

data class BudgetEditUiState(
    val budgetName: String = "Кубышка",
    val selectedPeriodIndex: Int = 0, // Индекс выбранного периода
    val periods: List<String> = listOf("2 мес", "3 мес", "4 мес", "6 мес", "Другой"),

    // Данные вклада
    val amount: String = "13 900",
    val currency: String = "Рубли",

    // Данные карты списания
    val sourceCardName: String = "Дебетовая карта",
    val sourceCardPan: String = "• 8563",
    val sourceCardBalance: String = "1 000 ₽",

    // Категории
    val categories: List<EditCategoryUi> = listOf(
        EditCategoryUi(1, "Маркетплейсы", "2 300 ₽", 0xFF42A5F5), // Blue
        EditCategoryUi(2, "Продукты", "14 600 ₽", 0xFF66BB6A)     // Green
    )
)

data class EditCategoryUi(
    val id: Long,
    val name: String,
    val limit: String,
    val color: Long
)