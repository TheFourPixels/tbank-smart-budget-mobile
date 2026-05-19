package com.tbank.smartbudget.feature.budget_tab.components

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
import androidx.compose.material3.LinearProgressIndicator
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
import com.tbank.smartbudget.core.ui.common.CategoryIconPlaceholder
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

data class SummaryCategoryUi(
    val id: Long,
    val name: String,
    val iconRes: Int,
    val color: Long
)

private fun getPluralCategories(count: Int): String {
    val mod10 = count % 10
    val mod100 = count % 100
    return when {
        mod100 in 11..19 -> "$count категорий"
        mod10 == 1 -> "$count категория"
        mod10 in 2..4 -> "$count категории"
        else -> "$count категорий"
    }
}

@Composable
fun SummaryRow(
    totalSpent: String,
    totalSpentDescription: String,
    spentProgress: Float,
    categories: List<SummaryCategoryUi>,
    onAllOperationsClick: () -> Unit,
    onSelectedCategoriesClick: () -> Unit
) {
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
                color = MaterialTheme.colorScheme.onBackground
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
                // Настоящий прогресс-бар
                LinearProgressIndicator(
                    progress = { spentProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    color = MaterialTheme.colorScheme.error,
                    trackColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                )
            }

            // Карточка "Выбранные категории" (применяем высоту)
            SummarySmallCard(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onSelectedCategoriesClick),
                minHeight = measuredHeightDp,
                containerColor = SmartBudgetTheme.colors.cardBackground
            ) {
                Text(
                    "Выбранные категории",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.W700,
                        lineHeight = 23.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))

                val totalCount = categories.size
                Text(
                    text = getPluralCategories(totalCount),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Spacer(Modifier.height(8.dp))

                // --- Иконки с наложением ---
                Row(
                    horizontalArrangement = Arrangement.spacedBy((-8).dp), // Наложение
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val maxIcons = 3

                    val showCount = if (totalCount > maxIcons) maxIcons - 1 else totalCount
                    val remainingCount = if (totalCount > maxIcons) totalCount - showCount else 0

                    // Рисуем видимые категории
                    categories.take(showCount).forEach { category ->
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(SmartBudgetTheme.colors.cardBackground)
                                .padding(2.dp)
                        ) {
                            CategoryIconPlaceholder(
                                color = Color(category.color),
                                iconRes = category.iconRes,
                                name = category.name,
                                size = 32.dp,
                                iconSize = 16.dp
                            )
                        }
                    }

                    // Если есть остаток, рисуем кружок "+N"
                    if (remainingCount > 0) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(SmartBudgetTheme.colors.cardBackground)
                                .padding(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+$remainingCount",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
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
