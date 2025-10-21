package com.tbank.smartbudget.domain.model

data class BudgetCategory(
    val id: Long,
    val name: String,
    val iconRes: Int,
    val color: Long
)