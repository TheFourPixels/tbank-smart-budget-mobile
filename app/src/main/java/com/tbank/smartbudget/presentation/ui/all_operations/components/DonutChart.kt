package com.tbank.smartbudget.presentation.ui.all_operations.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DonutChart(
    modifier: Modifier = Modifier.Companion,
    values: List<Float>,
    colors: List<Color>,
    strokeWidth: Dp = 28.dp // Чуть тоньше для соответствия стилю
) {
    Canvas(modifier = modifier) {
        val total = values.sum()
        var startAngle = -90f

        values.forEachIndexed { index, value ->
            val sweepAngle = (value / total) * 360f
            val gap = 17f // Зазор между сегментами

            drawArc(
                color = colors.getOrElse(index) { Color.Companion.Gray },
                startAngle = startAngle + gap / 2,
                sweepAngle = sweepAngle - gap,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Companion.Round)
            )
            startAngle += sweepAngle
        }
    }
}