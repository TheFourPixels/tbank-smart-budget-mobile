package com.tbank.smartbudget.presentation.ui.all_operations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.smartbudget.presentation.ui.all_operations.ChartDataUi

@Composable
fun CategoryTag(
    chartItem: ChartDataUi,
    isSelected: Boolean,
    isDimmed: Boolean, // Если true, элемент становится бледным
    onClick: () -> Unit
) {
    // Если элемент "затемнен", делаем его серым и прозрачным
    val baseColor = if (isDimmed) Color.Gray.copy(alpha = 0.3f) else chartItem.color
    val backgroundColor = if (isDimmed) Color(0xFFF5F5F5) else baseColor.copy(alpha = 0.15f)
    val textColor = if (isDimmed) Color.Gray else Color.Black.copy(alpha = 0.7f)

    // Если элемент выбран, можно добавить обводку или более яркий фон (опционально)
    // В текущем дизайне просто оставляем его цветным, пока остальные серые.

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(baseColor)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "${chartItem.categoryName} ${chartItem.amount}",
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}