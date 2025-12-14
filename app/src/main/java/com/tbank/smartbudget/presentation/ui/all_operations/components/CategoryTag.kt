package com.tbank.smartbudget.presentation.ui.all_operations.components

import androidx.compose.foundation.background
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
import com.tbank.smartbudget.presentation.ui.all_operations.OperationCategoryUi

@Composable
fun CategoryTag(category: OperationCategoryUi) {
    val categoryColor = Color(category.color)
    val backgroundColor = categoryColor.copy(alpha = 0.15f)

    Row(
        modifier = Modifier.Companion
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 12.dp),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        // Цветной круг (иконка)
        Box(
            modifier = Modifier.Companion
                .size(32.dp)
                .clip(CircleShape)
                .background(categoryColor)
        )
        Spacer(Modifier.Companion.width(8.dp))
        // Текст категории и сумма
        Text(
            text = "${category.name} ${category.amount}",
            color = Color.Companion.Black.copy(alpha = 0.7f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Companion.Medium
        )
    }
}