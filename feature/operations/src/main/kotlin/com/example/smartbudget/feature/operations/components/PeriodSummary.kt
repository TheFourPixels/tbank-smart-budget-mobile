package com.example.smartbudget.feature.operations.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun PeriodSummary(
    dateRangeLabel: String,
    totalExpense: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = dateRangeLabel,
            style = MaterialTheme.typography.labelLarge.copy(color = Color.Gray),
            modifier = Modifier.padding(start = 4.dp)
        )
        Text(
            text = totalExpense,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
        )
        Text(
            text = "Траты за период",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PeriodSummaryPreview() {
    SmartBudgetTheme {
        PeriodSummary(
            dateRangeLabel = "1 дек — 31 дек",
            totalExpense = "45 000 ₽"
        )
    }
}
