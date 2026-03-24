package com.tbank.smartbudget.core.network.remote.dto

import com.google.gson.annotations.SerializedName

data class GoalDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("targetAmount") val targetAmount: Double,
    @SerializedName("currentAmount") val currentAmount: Double,
    @SerializedName("deadline") val deadline: String?, // YYYY-MM-DD
    @SerializedName("icon") val icon: String?,
    @SerializedName("currency") val currency: String? = "RUB"
)

data class CreateGoalRequest(
    @SerializedName("name") val name: String,
    @SerializedName("targetAmount") val targetAmount: Double,
    @SerializedName("deadline") val deadline: String?
)

data class UpdateGoalAmountRequest(
    @SerializedName("amount") val amount: Double // Сумма пополнения (+ или -)
)