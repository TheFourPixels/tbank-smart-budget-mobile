package com.tbank.smartbudget.presentation.ui.budget_details

data class BudgetDetailsUiState(
    val budgetName: String = "Кубышка",
    val currentBalance: String = "13 900 ₽",
    val periodDescription: String = "На 2 месяца",

    // Блок "По вашему бюджету"
    val income: String = "12 300 ₽",
    val expenseLimit: String = "15 400 ₽",
    val freeFunds: String = "2 567 ₽",

    // Блок счета
    val linkedAccountName: String = "Black",
    val linkedAccountBalance: String = "35 600 ₽",

    // Настройки
    val isLimitNotificationEnabled: Boolean = true,
    val isOperationNotificationEnabled: Boolean = true
)