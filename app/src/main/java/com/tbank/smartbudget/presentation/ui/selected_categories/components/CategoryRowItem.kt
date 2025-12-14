package com.tbank.smartbudget.presentation.ui.selected_categories.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import com.tbank.smartbudget.presentation.ui.selected_categories.SelectedCategoryUi

@Composable
fun CategoryRowItem(category: SelectedCategoryUi) {
    Row(
        modifier = Modifier.Companion.fillMaxWidth(),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        // Иконка
        Box(
            modifier = Modifier.Companion
                .size(40.dp)
                .clip(CircleShape)
                .background(category.color),
            contentAlignment = Alignment.Companion.Center
        ) {
            Text("🛍️", fontSize = 20.sp) // Заглушка
        }

        Spacer(Modifier.Companion.width(12.dp))

        Column {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Companion.Bold,
                    fontSize = 16.sp
                ),
                color = Color.Companion.Black
            )
            Text(
                text = category.limitDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Companion.Gray
            )
        }
    }
}