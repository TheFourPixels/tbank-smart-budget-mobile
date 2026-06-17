package com.tbank.smartbudget.core.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculationsDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Как мы считаем",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                CalculationItem(
                    title = "Общий лимит",
                    description = "Это сумма всех лимитов по вашим категориям. Если лимиты не установлены, мы используем ваш планируемый доход."
                )
                Spacer(Modifier.height(16.dp))
                CalculationItem(
                    title = "Потрачено",
                    description = "Сумма всех ваших расходов за текущий месяц по всем категориям."
                )
                Spacer(Modifier.height(16.dp))
                CalculationItem(
                    title = "Осталось",
                    description = "Разница между общим лимитом и уже потраченными средствами. Это ваш запас на остаток месяца."
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Понятно", style = MaterialTheme.typography.labelLarge)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    )
}

@Composable
private fun CalculationItem(title: String, description: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
        )
    }
}
