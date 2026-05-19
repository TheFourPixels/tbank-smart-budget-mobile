package com.tbank.smartbudget.core.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ответ с пагинацией: GET /categories
 * Соответствует PageCategoryDto из Budget.json
 */
@Serializable
data class PageCategoryDto(
    @SerialName("content") val content: List<CategoryDto>,
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("totalElements") val totalElements: Long,
    @SerialName("last") val last: Boolean,
    @SerialName("number") val number: Int,
    @SerialName("size") val size: Int,
    @SerialName("numberOfElements") val numberOfElements: Int,
    @SerialName("first") val first: Boolean,
    @SerialName("empty") val empty: Boolean
)

/**
 * Модель категории с сервера
 */
@Serializable
data class CategoryDto(
    @SerialName("id") val id: Long? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("system") val isSystem: Boolean = false
)

/**
 * Тело запроса для создания/обновления: POST/PUT /categories
 */
@Serializable
data class CreateCategoryRequest(
    @SerialName("name") val name: String
)
