package com.tbank.smartbudget.presentation.ui.budget_tab.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SummarySmallCard(modifier: Modifier = Modifier.Companion, minHeight: Dp, content: @Composable ColumnScope.() -> Unit) {

    val shape = RoundedCornerShape(16.dp)

    // Определяем модификатор высоты: используем переданное значение, если оно есть
    val heightModifier = if (minHeight != Dp.Companion.Unspecified) Modifier.Companion.height(minHeight) else Modifier.Companion

    Box(
        modifier = modifier
            .then(heightModifier)
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = Color.Companion.Black.copy(alpha = 0.4f),
                spotColor = Color.Companion.Black.copy(alpha = 0.5f)
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = shape
            )
            .padding(16.dp)
    ) {
        Column(
            content = content
        )
    }
}