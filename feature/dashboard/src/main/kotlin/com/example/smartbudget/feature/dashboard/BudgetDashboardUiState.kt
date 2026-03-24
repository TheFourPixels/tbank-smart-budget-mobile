package com.example.smartbudget.feature.dashboard

data class BudgetDashboardUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    // Основные показатели
    val totalLimit: String = "0 ₽",
    val totalSpent: String = "0 ₽",
    val remainingAmount: String = "0 ₽",

    // Прогресс бар (0.0 - 1.0)
    val progress: Float = 0f,
    val progressColor: Long = 0xFF43A047,

    // Аналитика "на день"
    val daysLeft: Int = 0,
    val dailyBudget: String = "0 ₽",

    val periodDescription: String = ""
)