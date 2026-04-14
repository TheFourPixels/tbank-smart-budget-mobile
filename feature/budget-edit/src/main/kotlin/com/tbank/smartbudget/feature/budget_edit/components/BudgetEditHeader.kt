package com.tbank.smartbudget.feature.budget_edit.components

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
fun BudgetEditHeader(
    budgetName: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Бюджет “$budgetName”",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            color = Color.White
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2196F3)
@Composable
private fun BudgetEditHeaderPreview() {
    SmartBudgetTheme {
        BudgetEditHeader(budgetName = "Кубышка")
    }
}
