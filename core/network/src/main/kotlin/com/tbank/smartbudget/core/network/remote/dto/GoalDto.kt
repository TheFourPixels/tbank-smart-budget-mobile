package com.tbank.smartbudget.core.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoalDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("targetAmount") val targetAmount: Double,
    @SerialName("savedAmount") val savedAmount: Double,
    @SerialName("deadline") val deadline: String?, // date
    @SerialName("createdAt") val createdAt: String?, // date
    @SerialName("progressPercent") val progressPercent: Int,
    @SerialName("daysLeft") val daysLeft: Long,
    @SerialName("recommendedMonthly") val recommendedMonthly: Double,
    @SerialName("contributions") val contributions: List<GoalContributionDto>? = null
)

@Serializable
data class GoalContributionDto(
    @SerialName("amount") val amount: Double,
    @SerialName("date") val date: String,
    @SerialName("description") val description: String? = null
)

@Serializable
data class CreateGoalRequest(
    @SerialName("name") val name: String,
    @SerialName("targetAmount") val targetAmount: Double,
    @SerialName("deadline") val deadline: String? // date
)

@Serializable
data class GoalContributionRequest(
    @SerialName("amount") val amount: Double
)
