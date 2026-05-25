package com.tbank.smartbudget.data.domain.model

import androidx.compose.runtime.Immutable

@JvmInline value class GoalId(val value: Long)

@Immutable
data class Goal(
    val id: GoalId,
    val name: String,
    val targetAmount: Double,
    val savedAmount: Double,
    val deadline: String?,
    val createdAt: String? = null,
    val progressPercent: Int,
    val daysLeft: Long = 0,
    val recommendedMonthly: Double = 0.0,
    val contributions: List<GoalContribution> = emptyList()
)

@Immutable
data class GoalContribution(
    val amount: Double,
    val date: java.time.LocalDateTime,
    val description: String?
)
