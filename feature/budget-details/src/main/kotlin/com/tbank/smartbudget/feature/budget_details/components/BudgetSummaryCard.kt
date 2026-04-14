package com.tbank.smartbudget.feature.budget_details.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.smartbudget.core.ui.common.DetailsCard
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun BudgetSummaryCard(
    income: String,
    expenseLimit: String,
    freeFunds: String,
    onCalculationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        DetailsCard {
            Text(
                "По вашему бюджету",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow("Доход", income)
            Spacer(modifier = Modifier.height(12.dp))
            InfoRow("Общий лимит расходов", expenseLimit)
            Spacer(modifier = Modifier.height(12.dp))
            InfoRow("Свободные средства", freeFunds)
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onCalculationsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFDD2D),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Посмотреть расчеты",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetSummaryCardPreview() {
    SmartBudgetTheme {
        BudgetSummaryCard(
            income = "12 300 ₽",
            expenseLimit = "15 400 ₽",
            freeFunds = "2 567 ₽",
            onCalculationsClick = {}
        )
    }
}
