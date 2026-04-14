package com.example.smartbudget.feature.dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun BudgetDashboardHero(
    remainingAmount: String,
    budgetName: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Денег осталось \n$remainingAmount",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White.copy(alpha = 0.9f)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "анализируем бюджет \"$budgetName\"",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f)
        )

        Spacer(Modifier.height(24.dp))

        // Мок-данные для графика
        val mockChartData = remember {
            listOf(1.0f, 0.95f, 0.88f, 0.82f, 0.75f, 0.60f, 0.55f, 0.48f, 0.40f, 0.45f)
        }

        BudgetLineChart(
            dataPoints = mockChartData,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            lineColor = Color.White,
            dotColor = SmartBudgetTheme.colors.gradientYellow
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2196F3)
@Composable
private fun BudgetDashboardHeroPreview() {
    SmartBudgetTheme {
        BudgetDashboardHero(
            remainingAmount = "17 500 ₽",
            budgetName = "Кубышка"
        )
    }
}
