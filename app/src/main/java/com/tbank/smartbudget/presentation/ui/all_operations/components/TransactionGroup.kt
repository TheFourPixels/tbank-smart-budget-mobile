package com.tbank.smartbudget.presentation.ui.all_operations.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.presentation.ui.all_operations.TransactionGroupUi

@Composable
fun TransactionGroup(group: TransactionGroupUi) {
    Column {
        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                group.date,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Companion.Bold)
            )
            Text(
                group.totalAmount,
                style = MaterialTheme.typography.titleMedium.copy(color = Color.Companion.LightGray)
            )
        }
        Spacer(Modifier.Companion.height(16.dp))

        group.items.forEach { transaction ->
            TransactionItem(transaction)
            Spacer(Modifier.Companion.height(16.dp))
        }
    }
}