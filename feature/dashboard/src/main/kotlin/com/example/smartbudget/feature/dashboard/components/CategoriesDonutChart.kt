package com.example.smartbudget.feature.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.smartbudget.feature.dashboard.categories.CategoryDashboardItem

@Composable
fun CategoriesDonutChart(
    categories: List<CategoryDashboardItem>,
    modifier: Modifier = Modifier
) {
    val maxIndex = categories.indices.maxByOrNull { categories[it].amountValue } ?: -1

    Canvas(modifier = modifier) {
        val total = categories.sumOf { it.amountValue }
        var startAngle = -90f
        val gapAngle = 3f

        val baseStrokeWidth = 20.dp.toPx()
        val maxStrokeWidth = 30.dp.toPx()

        if (total == 0.0) {
            drawCircle(
                color = Color.LightGray.copy(alpha = 0.3f),
                style = Stroke(width = baseStrokeWidth)
            )
        } else {
            categories.forEachIndexed { index, item ->
                val sweepAngleRaw = (item.amountValue / total).toFloat() * 360f
                val sweepAngle =
                    if (sweepAngleRaw > gapAngle) sweepAngleRaw - gapAngle else sweepAngleRaw

                val isMax = index == maxIndex
                val currentStrokeWidth = if (isMax) maxStrokeWidth else baseStrokeWidth

                if (sweepAngle > 0) {
                    drawArc(
                        color = item.color,
                        startAngle = startAngle + (gapAngle / 2),
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = currentStrokeWidth, cap = StrokeCap.Butt)
                    )
                    startAngle += sweepAngleRaw
                }
            }
        }
    }
}