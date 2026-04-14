package com.example.smartbudget.feature.dashboard.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun BudgetStatusCard(
    isOverBudget: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isOverBudget) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = if (isOverBudget) "Вы превысили лимит бюджета!" else "Вы идете по плану. Так держать!",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetStatusCardOnTrackPreview() {
    SmartBudgetTheme {
        BudgetStatusCard(isOverBudget = false)
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetStatusCardOverBudgetPreview() {
    SmartBudgetTheme {
        BudgetStatusCard(isOverBudget = true)
    }
}
