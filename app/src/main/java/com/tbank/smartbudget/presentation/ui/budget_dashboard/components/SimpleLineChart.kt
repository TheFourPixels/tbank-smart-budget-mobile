package com.tbank.smartbudget.presentation.ui.budget_dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun SimpleLineChart(
    dataPoints: List<Float>,
    lineColor: Color
) {
    if (dataPoints.isEmpty()) return

    Canvas(modifier = Modifier.Companion.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val maxVal = dataPoints.maxOrNull() ?: 1f
        val stepX = width / (dataPoints.size - 1)

        val path = Path().apply {
            moveTo(0f, height - (dataPoints.first() / maxVal) * height)
            for (i in 1 until dataPoints.size) {
                val x = i * stepX
                val y = height - (dataPoints[i] / maxVal) * height

                val prevX = (i - 1) * stepX
                val prevY = height - (dataPoints[i - 1] / maxVal) * height

                val cx1 = prevX + stepX / 2
                val cy1 = prevY
                val cx2 = prevX + stepX / 2
                val cy2 = y

                cubicTo(cx1, cy1, cx2, cy2, x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Companion.Round,
                join = StrokeJoin.Companion.Round
            )
        )

        val fillPath = Path().apply {
            addPath(path)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.Companion.verticalGradient(
                colors = listOf(lineColor.copy(alpha = 0.2f), Color.Companion.Transparent),
                startY = 0f,
                endY = height
            )
        )
    }
}