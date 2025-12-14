package com.tbank.smartbudget.presentation.ui.all_operations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

@Composable
fun FilterChip(text: String, isBlue: Boolean) {
    Box(
        modifier = Modifier.Companion
            .background(
                color = if (isBlue) SmartBudgetTheme.colors.blue else Color(0xFFF5F5F5),
                shape = RoundedCornerShape(50) // Изменено на 50% (полностью круглые края)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp), // Увеличены отступы
        contentAlignment = Alignment.Companion.Center
    ) {
        Row(verticalAlignment = Alignment.Companion.CenterVertically) {
            Text(
                text = text,
                color = if (isBlue) Color.Companion.White else Color.Companion.Black,
                fontWeight = FontWeight.Companion.Medium
            )
            Spacer(Modifier.Companion.width(4.dp))
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = if (isBlue) Color.Companion.White else Color.Companion.Black,
                modifier = Modifier.Companion.size(20.dp) // Чуть крупнее иконка
            )
        }
    }
}