package com.tbank.smartbudget.core.ui.common


object CategoryColorMapper {
    fun getColorForId(id: Long): Long {
        val colors = listOf(
            0xFF43A047, 0xFF1E88E5, 0xFFE53935, 0xFFFB8C00,
            0xFF8E24AA, 0xFF00ACC1, 0xFFD81B60, 0xFF546E7A
        )
        return colors[(id % colors.size).toInt()]
    }
}