package com.tbank.smartbudget.presentation.ui.budget_edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UnitSwitchBox(
    isPercentMode: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF9F9F9))
            .clickable(onClick = onToggle)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Text(
            text = "Единицы",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Companion.SemiBold),
            color = Color.Companion.Black // Лейбл черный, как в isValueSecondary=true
        )
        Spacer(Modifier.Companion.height(4.dp))

        // Ряд с переключателем
        Row(verticalAlignment = Alignment.Companion.CenterVertically) {
            // Рубли
            Text(
                text = "₽",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = if (!isPercentMode) FontWeight.Companion.Bold else FontWeight.Companion.Normal,
                    color = if (!isPercentMode) Color.Companion.Black else Color.Companion.Gray,
                    fontSize = 16.sp
                )
            )

            Text(
                text = " / ",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.Companion.Gray,
                    fontSize = 16.sp
                )
            )

            // Проценты
            Text(
                text = "%",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = if (isPercentMode) FontWeight.Companion.Bold else FontWeight.Companion.Normal,
                    color = if (isPercentMode) Color.Companion.Black else Color.Companion.Gray,
                    fontSize = 16.sp
                )
            )
        }
    }
}