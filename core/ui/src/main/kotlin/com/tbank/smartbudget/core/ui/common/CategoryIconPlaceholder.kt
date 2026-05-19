package com.tbank.smartbudget.core.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryIconPlaceholder(
    color: Color,
    iconRes: Int?,
    name: String = "",
    size: Dp = 40.dp,
    iconSize: Dp = 20.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        if (iconRes != null && iconRes != 0) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(iconSize)
            )
        } else {
            // Fallback to emoji based on name
            val emoji = when {
                name.contains("Продукт", ignoreCase = true) -> "🛍️"
                name.contains("Транспорт", ignoreCase = true) -> "🚌"
                name.contains("Кафе", ignoreCase = true) || name.contains("Ресторан", ignoreCase = true) -> "🍴"
                name.contains("Развлеч", ignoreCase = true) -> "🎉"
                name.contains("Здоров", ignoreCase = true) -> "💊"
                name.contains("Образов", ignoreCase = true) -> "📚"
                name.contains("Одежд", ignoreCase = true) -> "👕"
                name.contains("Маркет", ignoreCase = true) -> "📦"
                else -> "💰"
            }
            Text(text = emoji, fontSize = (iconSize.value).sp)
        }
    }
}
