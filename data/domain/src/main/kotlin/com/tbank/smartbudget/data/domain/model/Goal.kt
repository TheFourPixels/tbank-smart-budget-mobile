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
    val progressPercent: Int
)