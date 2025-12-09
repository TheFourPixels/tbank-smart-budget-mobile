package com.tbank.smartbudget.presentation.ui.budget_tab.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryIconPlaceholder(color: Color) {
    Box(
        modifier = Modifier.Companion
            .size(32.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Companion.Center
    ) {
        // Эмодзи-заглушка для иконки
        Text("🛒", fontSize = 16.sp)
    }
}