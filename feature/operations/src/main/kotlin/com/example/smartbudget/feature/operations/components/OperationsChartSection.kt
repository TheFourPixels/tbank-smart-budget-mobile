package com.example.smartbudget.feature.operations.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartbudget.feature.operations.ChartDataUi
import com.example.smartbudget.feature.operations.PeriodType
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OperationsChartSection(
    chartData: List<ChartDataUi>,
    selectedCategoryNames: Set<String>,
    periodType: PeriodType,
    onPeriodChanged: (PeriodType) -> Unit,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentAlignment = Alignment.Center
        ) {
            if (chartData.isNotEmpty()) {
                DonutChart(
                    modifier = Modifier.size(200.dp),
                    chartData = chartData,
                    selectedCategoryNames = selectedCategoryNames
                )
            } else {
                Text("Нет данных за этот период", color = Color.Gray)
            }
        }

        Spacer(Modifier.height(16.dp))

        PeriodToggle(
            selectedType = periodType,
            onTypeSelected = onPeriodChanged
        )

        Spacer(Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            chartData.forEach { chartItem ->
                val isSelected = selectedCategoryNames.contains(chartItem.categoryName)
                val isAnySelected = selectedCategoryNames.isNotEmpty()
                CategoryTag(
                    chartItem = chartItem,
                    isSelected = isSelected,
                    isDimmed = isAnySelected && !isSelected,
                    onClick = { onCategorySelected(chartItem.categoryName) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OperationsChartSectionPreview() {
    SmartBudgetTheme {
        OperationsChartSection(
            chartData = listOf(
                ChartDataUi("Еда", "15 000 ₽", Color.Red, 0.4f),
                ChartDataUi("Транспорт", "5 000 ₽", Color.Blue, 0.15f)
            ),
            selectedCategoryNames = emptySet(),
            periodType = PeriodType.MONTH,
            onPeriodChanged = {},
            onCategorySelected = {}
        )
    }
}
