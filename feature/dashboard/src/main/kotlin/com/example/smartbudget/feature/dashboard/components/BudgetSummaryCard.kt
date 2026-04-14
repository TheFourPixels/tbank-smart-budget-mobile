package com.example.smartbudget.feature.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
    totalLimit: String,
    remainingAmount: String,
    totalSpent: String,
    progressColor: Long,
    onCalculationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        DetailsCard {
            Text(
                "Сводка по бюджету",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                SummaryRow("Было денег", totalLimit)
                Spacer(Modifier.height(8.dp))
                SummaryRow("Осталось денег", remainingAmount)
                Spacer(Modifier.height(8.dp))
                SummaryRow("Потрачено", totalSpent, valueColor = Color(progressColor))
                Spacer(Modifier.height(8.dp))
                SummaryRow("Получено", "0 ₽", valueColor = SmartBudgetTheme.colors.gradientGreen)
            }
            Spacer(Modifier.height(20.dp))

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

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    valueColor: Color = Color.Unspecified
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetSummaryCardPreview() {
    SmartBudgetTheme {
        BudgetSummaryCard(
            totalLimit = "30 000 ₽",
            remainingAmount = "17 500 ₽",
            totalSpent = "12 500 ₽",
            progressColor = 0xFF43A047,
            onCalculationsClick = {}
        )
    }
}
