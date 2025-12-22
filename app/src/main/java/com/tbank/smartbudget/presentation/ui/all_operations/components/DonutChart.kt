package com.tbank.smartbudget.presentation.ui.all_operations.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.presentation.ui.all_operations.ChartDataUi

@Composable
fun DonutChart(
    modifier: Modifier = Modifier.Companion,
    chartData: List<ChartDataUi>,
    selectedCategoryNames: Set<String>,
    strokeWidth: Dp = 28.dp
) {
    Canvas(modifier = modifier) {
        var startAngle = -90f
        val dimmedColor = Color.Companion.LightGray.copy(alpha = 0.3f)
        chartData.forEach { dataItem ->
            val percent = dataItem.percentage
            val sweepAngle = percent * 360f
            val gap = if (chartData.size > 1) 2f else 0f
            val segmentColor =
                if (selectedCategoryNames.isEmpty() || selectedCategoryNames.contains(dataItem.categoryName)) dataItem.color else dimmedColor
            drawArc(
                color = segmentColor,
                startAngle = startAngle + gap / 2,
                sweepAngle = sweepAngle - gap,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Companion.Round)
            )
            startAngle += sweepAngle
        }
    }
}