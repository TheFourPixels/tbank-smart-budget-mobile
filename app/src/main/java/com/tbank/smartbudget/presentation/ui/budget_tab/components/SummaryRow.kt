package com.tbank.smartbudget.presentation.ui.budget_tab.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

// Модель данных для этой карточки
data class SummaryCategoryUi(
    val id: Long,
    val name: String,
    val iconRes: Int,
    val color: Long
)

@Composable
fun SummaryRow(
    totalSpent: String,
    totalSpentDescription: String,
    categories: List<SummaryCategoryUi>,
    onAllOperationsClick: () -> Unit,
    onSelectedCategoriesClick: () -> Unit
) {
    // Состояние для хранения измеренной высоты
    var measuredHeightDp by remember { mutableStateOf(Dp.Unspecified) }
    val density = LocalDensity.current

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {

        // --- Заголовок "Траты" ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Траты",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
        }

        Spacer(Modifier.height(12.dp))

        // --- Карточки ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Карточка "Все операции" (измеряем высоту)
            SummarySmallCard(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onAllOperationsClick)
                    .onGloballyPositioned { coordinates ->
                        if (measuredHeightDp == Dp.Unspecified) {
                            measuredHeightDp = with(density) { coordinates.size.height.toDp() }
                        }
                    },
                minHeight = measuredHeightDp
            ) {
                Text(
                    "Все операции",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "$totalSpentDescription\n$totalSpent",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 23.sp,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(20.dp))
                // Имитация прогресс-бара
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.error)
                )
            }

            // Карточка "Выбранные категории" (применяем высоту)
            SummarySmallCard(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onSelectedCategoriesClick),
                minHeight = measuredHeightDp
            ) {
                Text(
                    "Выбранные категории",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.W700,
                        lineHeight = 23.sp
                    )
                )
                Spacer(Modifier.height(8.dp))

                // --- Иконки с наложением ---
                Row(
                    horizontalArrangement = Arrangement.spacedBy((-8).dp), // Наложение
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val maxIcons = 3
                    val totalCount = categories.size

                    // Логика:
                    // Если всего категорий <= 4, показываем все
                    // Если больше 4, показываем 3 иконки + кружок "+N"

                    val showCount = if (totalCount > maxIcons) maxIcons - 1 else totalCount
                    val remainingCount = if (totalCount > maxIcons) totalCount - showCount else 0

                    // Рисуем видимые категории
                    categories.take(showCount).forEach { category ->
                        // Белая обводка для эффекта разделения при наложении
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(2.dp)
                        ) {
                            CategoryIconPlaceholder(
                                color = Color(category.color),
                                iconRes = category.iconRes
                            )
                        }
                    }

                    // Если есть остаток, рисуем кружок "+N"
                    if (remainingCount > 0) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp) // Размер как у CategoryIconPlaceholder
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0E0E0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+$remainingCount",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}