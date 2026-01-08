package com.tbank.smartbudget.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Ответ с пагинацией: GET /categories
 */
data class CategoryPagedResponse(
    @SerializedName("content") val content: List<CategoryDto>,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("last") val last: Boolean
)

/**
 * Модель категории с сервера
 */
data class CategoryDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("isSystem") val isSystem: Boolean
)

/**
 * Тело запроса для создания/обновления: POST/PUT /categories
 */
data class CreateCategoryRequest(
    @SerializedName("name") val name: String
)