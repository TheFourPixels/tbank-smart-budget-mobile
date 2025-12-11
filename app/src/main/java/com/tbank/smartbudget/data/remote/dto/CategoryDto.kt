package com.tbank.smartbudget.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.tbank.smartbudget.domain.model.BudgetCategory

/**
 * Ответ сервера при запросе списка категорий.
 * JSON API: { "content": [ ... ] }
 */
data class CategoryResponseDto(
    @SerializedName("content")
    val content: List<CategoryDto>
)

/**
 * DTO одной категории.
 * JSON API: { "id": 1, "name": "Продукты" }
 */
data class CategoryDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
) {
    // Функция маппинга в доменную модель
    fun toDomain(): BudgetCategory {
        return BudgetCategory(
            id = id,
            name = name,
            iconRes = 0, // Сервер пока не возвращает иконку
            // Генерируем цвет или ставим дефолтный, т.к. сервер его не возвращает
            color = generateColorById(id)
        )
    }

    // Временная заглушка для цветов, чтобы UI был красивым
    private fun generateColorById(id: Long): Long {
        val colors = listOf(
            0xFF43A047, // Green
            0xFF1E88E5, // Blue
            0xFFFBC02D, // Yellow
            0xFFE53935, // Red
            0xFF8E24AA, // Purple
            0xFF00ACC1, // Cyan
            0xFFFF7043  // Orange
        )
        return colors[(id % colors.size).toInt()]
    }
}