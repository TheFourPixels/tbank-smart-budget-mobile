package com.example.smartbudget.feature.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartbudget.feature.dashboard.categories.CategoryDashboardItem

@Composable
fun CategoriesHorizontalBarChartPercent(
    categories: List<CategoryDashboardItem>
) {
    if (categories.isEmpty()) return

    // Вычисляем максимальное значение для масштабирования столбцов (чтобы самый большой занимал 100% ширины)
    val maxAmount = categories.maxOfOrNull { it.amountValue } ?: 1.0

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        categories.forEach { item ->
            // Строка с названием и баром
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Название категории (слева, фиксированной ширины)
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.width(100.dp),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Горизонтальный столбец
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Вычисляем длину столбца относительно МАКСИМАЛЬНОГО элемента
                    val fillFraction = (item.amountValue / maxAmount).toFloat().coerceIn(0.01f, 1f)

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Цветная часть (столбец)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fillFraction)
                                .fillMaxHeight()
                                .background(
                                    color = item.color,
                                    shape = RoundedCornerShape(
                                        topStart = 0.dp,
                                        bottomStart = 0.dp,
                                        topEnd = 12.dp, // Половина высоты (24.dp)
                                        bottomEnd = 12.dp
                                    )
                                ),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            // Процент внутри, если бар достаточно широкий
                            if (fillFraction > 0.2f) {
                                Text(
                                    text = "${(item.percent * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.Black, // Черный текст (было Color.White, но пользователь просил черный)
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        }

                        // Процент снаружи, если бар узкий
                        if (fillFraction <= 0.2f) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${(item.percent * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}