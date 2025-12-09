package com.tbank.smartbudget.presentation.ui.budget_tab.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.tbank.smartbudget.presentation.ui.setup.BudgetTabCategoryUi

@Composable
fun SummaryRow(totalSpent: String, totalSpentDescription: String, selectedCategories: List<BudgetTabCategoryUi>) {
    // Состояние для хранения измеренной высоты
    var measuredHeightDp by remember { mutableStateOf(Dp.Companion.Unspecified) }
    val density = LocalDensity.current

    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Companion.Top
    ) {
        // Карточка "Все операции" (измеряем высоту)
        SummarySmallCard(
            modifier = Modifier.Companion
                .weight(1f)
                //Измеряем высоту этой карточки
                .onGloballyPositioned { coordinates ->
                    if (measuredHeightDp == Dp.Companion.Unspecified) {
                        // Переводим высоту из пикселей в Dp
                        measuredHeightDp = with(density) { coordinates.size.height.toDp() }
                    }
                },
            minHeight = measuredHeightDp // Передаем измеренную высоту (если она уже есть)
        ) {
            Text(
                "Все операции",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Companion.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.Companion.height(6.dp))
            Text(
                "$totalSpentDescription\n$totalSpent",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 23.sp,
                fontSize = 16.sp
            )
            Spacer(Modifier.Companion.height(20.dp))
            // Имитация прогресс-бара
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.error)
            )
        }

        // Карточка "Выбранные категории" (применяем высоту)
        SummarySmallCard(
            modifier = Modifier.Companion.weight(1f),
            minHeight = measuredHeightDp // Применяем высоту, измеренную первой карточкой
        ) {
            Text(
                "Выбранные категории",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Companion.W700,
                    lineHeight = 23.sp
                )
            )
            Spacer(Modifier.Companion.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                selectedCategories.take(2).forEach { category ->
                    CategoryIconPlaceholder(Color(category.color))
                }
            }
        }
    }
}