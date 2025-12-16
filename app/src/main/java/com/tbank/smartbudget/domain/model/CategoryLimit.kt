package com.tbank.smartbudget.domain.model
/**
 * Модель лимита категории для ОТОБРАЖЕНИЯ (Dashboard).
 * Содержит информацию о том, сколько потрачено.
 */
data class CategoryLimit(
    val id: Long,
    val name: String,
    val limitAmount: Double,
    val spentAmount: Double,
    val iconRes: Int,
    val color: Long
) {
    val usagePercentage: Double
        get() = if (limitAmount > 0) spentAmount / limitAmount else 0.0

    val remainingAmount: Double
        get() = limitAmount - spentAmount
}
