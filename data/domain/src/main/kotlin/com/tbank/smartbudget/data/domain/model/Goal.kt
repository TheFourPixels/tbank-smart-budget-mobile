package com.tbank.smartbudget.data.domain.model

data class Goal(
    val id: Long,
    val name: String,
    val targetAmount: Double,
    val savedAmount: Double,
    val deadline: String?,
    val progressPercent: Int
)