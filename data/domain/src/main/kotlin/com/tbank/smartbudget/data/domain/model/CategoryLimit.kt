package com.tbank.smartbudget.data.domain.model

import androidx.compose.runtime.Immutable

/**
 * Модель лимита категории для ОТОБРАЖЕНИЯ (Dashboard).
 * Содержит информацию о том, сколько потрачено.
 */

@Immutable
data class CategoryLimit(
    val id: CategoryId,
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
