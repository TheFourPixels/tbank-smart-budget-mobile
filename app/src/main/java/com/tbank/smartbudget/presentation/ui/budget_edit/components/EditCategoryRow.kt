package com.tbank.smartbudget.presentation.ui.budget_edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.presentation.ui.budget_edit.EditCategoryUi

@Composable
fun EditCategoryRow(category: EditCategoryUi) {
    Row(
        modifier = Modifier.Companion.fillMaxWidth(),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        // Иконка
        Box(
            modifier = Modifier.Companion
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(category.color)),
            contentAlignment = Alignment.Companion.Center
        ) {
            // Иконка-заглушка
            Icon(
                // В реальности здесь будет category.iconRes
                imageVector = Icons.Default.ArrowBack, // TODO: Заменить на иконку категории
                contentDescription = null,
                tint = Color.Companion.White,
                modifier = Modifier.Companion.size(24.dp)
            )
        }

        Spacer(Modifier.Companion.width(16.dp))

        Column {
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Companion.SemiBold)
            )
            Text(
                text = "Лимит: ${category.limit}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Companion.Gray
            )
        }
    }
}