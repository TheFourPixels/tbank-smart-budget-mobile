package com.tbank.smartbudget.core.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoalDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("targetAmount") val targetAmount: Double,
    @SerialName("currentAmount") val currentAmount: Double,
    @SerialName("deadline") val deadline: String?, // YYYY-MM-DD
    @SerialName("icon") val icon: String?,
    @SerialName("currency") val currency: String? = "RUB"
)

@Serializable
data class CreateGoalRequest(
    @SerialName("name") val name: String,
    @SerialName("targetAmount") val targetAmount: Double,
    @SerialName("deadline") val deadline: String?
)

@Serializable
data class UpdateGoalAmountRequest(
    @SerialName("amount") val amount: Double // Сумма пополнения (+ или -)
)