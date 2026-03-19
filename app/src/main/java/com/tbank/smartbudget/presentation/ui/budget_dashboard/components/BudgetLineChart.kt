package com.tbank.smartbudget.presentation.ui.budget_dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

// Компонент для рисования ПЛАВНОГО графика
@Composable
fun BudgetLineChart(
    dataPoints: List<Float>,
    modifier: Modifier = Modifier.Companion,
    lineColor: Color,
    dotColor: Color
) {
    Canvas(modifier = modifier) {
        if (dataPoints.isEmpty()) return@Canvas

        val width = size.width
        val height = size.height
        // Шаг по оси X
        val stepX = width / (dataPoints.size - 1).coerceAtLeast(1)

        // 1. Создаем путь для линии (используем cubicTo для плавности)
        val strokePath = Path().apply {
            val startY = height * (1 - dataPoints.first())
            moveTo(0f, startY)

            for (i in 0 until dataPoints.size - 1) {
                val p1 = dataPoints[i]
                val p2 = dataPoints[i + 1]

                val x1 = i * stepX
                val y1 = height * (1 - p1)
                val x2 = (i + 1) * stepX
                val y2 = height * (1 - p2)

                // Контрольные точки для кривой Безье (середина по X)
                val cx1 = x1 + stepX / 2
                val cy1 = y1
                val cx2 = x1 + stepX / 2
                val cy2 = y2

                cubicTo(cx1, cy1, cx2, cy2, x2, y2)
            }
        }

        // Рисуем ТОЛЬКО линию, с градиентом
        drawPath(
            path = strokePath,
            brush = Brush.Companion.horizontalGradient(
                colors = listOf(
                    lineColor.copy(alpha = 0.4f),
                    lineColor.copy(alpha = 1.0f)
                ),
                startX = 0f,
                endX = width
            ),
            style = Stroke(
                width = 5.dp.toPx(),
                cap = StrokeCap.Companion.Round,
                join = StrokeJoin.Companion.Round
            )
        )

        // Рисуем точку только для ПОСЛЕДНЕГО значения
        if (dataPoints.isNotEmpty()) {
            val i = dataPoints.lastIndex
            val p = dataPoints.last()
            val center = Offset(i * stepX, height * (1 - p))

            drawCircle(
                color = Color.Companion.White,
                radius = 6.dp.toPx(),
                center = center
            )
            drawCircle(
                color = dotColor,
                radius = 6.dp.toPx(),
                center = center,
                style = Stroke(width = 3.dp.toPx())
            )
        }
    }
}