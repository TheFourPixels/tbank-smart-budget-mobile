package com.tbank.smartbudget.data.domain.model

object CategoryColorMapper {
    private val colors = listOf(
        0xFFEF5350, 0xFFEC407A, 0xFFAB47BC, 0xFF7E57C2, 0xFF5C6BC0,
        0xFF42A5F5, 0xFF29B6F6, 0xFF26C6DA, 0xFF26A69A, 0xFF66BB6A,
        0xFF9CCC65, 0xFFD4E157, 0xFFFFEE58, 0xFFFFCA28, 0xFFFFA726,
        0xFFFF7043, 0xFF8D6E63, 0xFFBDBDBD, 0xFF78909C
    )

    fun getColorForId(id: Long): Long {
        if (id <= 0L) return 0xFFBDBDBD // Default gray for uncategorized
        val index = (id % colors.size).toInt()
        return colors[if (index < 0) index + colors.size else index]
    }

    fun getColorForName(name: String): Long {
        if (name.isEmpty() || name == "Без категории") return 0xFFBDBDBD
        val hash = name.hashCode().toLong()
        val index = (hash % colors.size).toInt()
        return colors[if (index < 0) index + colors.size else index]
    }
}
