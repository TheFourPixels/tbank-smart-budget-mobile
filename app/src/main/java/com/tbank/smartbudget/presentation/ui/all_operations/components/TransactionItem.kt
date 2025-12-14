package com.tbank.smartbudget.presentation.ui.all_operations.components

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
import com.tbank.smartbudget.presentation.ui.all_operations.TransactionUi

@Composable
fun TransactionItem(transaction: TransactionUi) {
    Row(
        modifier = Modifier.Companion.fillMaxWidth(),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Box(
            modifier = Modifier.Companion
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(transaction.iconColor)),
            contentAlignment = Alignment.Companion.Center
        ) {
            // Заглушка
        }

        Spacer(Modifier.Companion.width(12.dp))

        Column(modifier = Modifier.Companion.weight(1f)) {
            Text(transaction.name, fontWeight = FontWeight.Companion.Bold)
            Text(
                transaction.categoryName,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Companion.Gray
            )
        }

        Text(transaction.amount, fontWeight = FontWeight.Companion.Bold)
    }
}