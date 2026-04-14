package com.tbank.smartbudget.core.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageDto<T>(
    @SerialName("content") val content: List<T>,
    @SerialName("totalElements") val totalElements: Long,
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("last") val last: Boolean,
    @SerialName("first") val first: Boolean,
    @SerialName("size") val size: Int,
    @SerialName("number") val number: Int,
    @SerialName("numberOfElements") val numberOfElements: Int,
    @SerialName("empty") val empty: Boolean
)