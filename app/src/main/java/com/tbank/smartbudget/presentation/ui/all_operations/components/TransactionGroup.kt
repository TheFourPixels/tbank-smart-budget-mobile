package com.tbank.smartbudget.presentation.ui.all_operations.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.presentation.ui.all_operations.TransactionGroupUi

@Composable
fun TransactionGroup(group: TransactionGroupUi) {
    Column {
        Text(group.dateHeader, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(Modifier.height(16.dp))
        group.items.forEach { transaction -> TransactionItem(transaction); Spacer(Modifier.height(16.dp)) }
    }
}