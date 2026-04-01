package com.tbank.smartbudget.data.domain.model

@JvmInline value class CategoryId(val value: Long)


data class BudgetCategory(
    val id: CategoryId,
    val name: String,
    val iconRes: Int,
    val color: Long
)