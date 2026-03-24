package com.tbank.smartbudget.feature.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun PinIndicator(filledCount: Int, totalCount: Int = 4) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(totalCount) { index ->
            val isFilled = index < filledCount
            val color = if (isFilled) SmartBudgetTheme.colors.blue else Color(0xFFE0E0E0)

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}