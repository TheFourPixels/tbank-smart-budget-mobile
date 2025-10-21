package com.tbank.smartbudget.domain.model

data class BudgetSummary(
    val totalIncome: Double,
    val totalLimit: Double,
    val totalSpent: Double,
    val freeFunds: Double // totalIncome - totalLimit
)