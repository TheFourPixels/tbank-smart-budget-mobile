package com.tbank.smartbudget.core.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ответ с пагинацией: GET /categories
 */
data class CategoryPagedResponse(
    @SerialName("content") val content: List<CategoryDto>,
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("totalElements") val totalElements: Int,
    @SerialName("last") val last: Boolean
)

/**
 * Модель категории с сервера
 */
@Serializable
data class CategoryDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("isSystem") val isSystem: Boolean
)

/**
 * Тело запроса для создания/обновления: POST/PUT /categories
 */
@Serializable
data class CreateCategoryRequest(
    @SerialName("name") val name: String
)