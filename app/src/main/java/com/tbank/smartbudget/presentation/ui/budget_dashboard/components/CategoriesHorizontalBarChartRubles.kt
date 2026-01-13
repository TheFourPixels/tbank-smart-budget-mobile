package com.tbank.smartbudget.presentation.ui.budget_dashboard.components

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
import com.tbank.smartbudget.presentation.ui.budget_dashboard.categories.CategoryDashboardItem

@Composable
fun CategoriesHorizontalBarChartRubles(
    categories: List<CategoryDashboardItem>
) {
    if (categories.isEmpty()) return

    // Вычисляем максимальное значение для масштабирования столбцов
    val maxAmount = categories.maxOfOrNull { it.amountValue } ?: 1.0

    Column(
        modifier = Modifier.Companion.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        categories.forEach { item ->
            // Строка с названием и баром
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                // Название категории (слева, фиксированной ширины)
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Companion.Black,
                    modifier = Modifier.Companion.width(100.dp),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.Companion.width(8.dp))

                // Горизонтальный столбец
                Box(
                    modifier = Modifier.Companion
                        .weight(1f)
                        .height(24.dp),
                    contentAlignment = Alignment.Companion.CenterStart
                ) {
                    // Вычисляем длину столбца относительно МАКСИМАЛЬНОГО элемента
                    val fillFraction = (item.amountValue / maxAmount).toFloat().coerceIn(0.01f, 1f)

                    Row(
                        modifier = Modifier.Companion.fillMaxSize(),
                        verticalAlignment = Alignment.Companion.CenterVertically
                    ) {
                        // Цветная часть (столбец)
                        Box(
                            modifier = Modifier.Companion
                                .fillMaxWidth(fillFraction)
                                .fillMaxHeight()
                                .background(
                                    color = item.color,
                                    shape = RoundedCornerShape(
                                        topStart = 0.dp,
                                        bottomStart = 0.dp,
                                        topEnd = 12.dp, // Закругление справа
                                        bottomEnd = 12.dp
                                    )
                                ),
                            contentAlignment = Alignment.Companion.CenterEnd
                        ) {
                            // Сумма внутри, если бар достаточно широкий
                            if (fillFraction > 0.4f) {
                                Text(
                                    text = item.amountStr, // ОТОБРАЖАЕМ СУММУ В РУБЛЯХ
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Companion.Bold),
                                    color = Color.Companion.Black,
                                    modifier = Modifier.Companion.padding(end = 8.dp)
                                )
                            }
                        }

                        // Сумма снаружи, если бар узкий
                        if (fillFraction <= 0.4f) {
                            Spacer(modifier = Modifier.Companion.width(8.dp))
                            Text(
                                text = item.amountStr, // ОТОБРАЖАЕМ СУММУ В РУБЛЯХ
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Companion.Bold),
                                color = Color.Companion.Black
                            )
                        }
                    }
                }
            }
        }
    }
}