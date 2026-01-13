package com.tbank.smartbudget.presentation.ui.budget_dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsBottomSheet(
    onDismiss: () -> Unit,
    onChartSelected: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp) // Отступ снизу для безопасной зоны
        ) {
            Text(
                text = "Доступные диаграммы",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )

            Spacer(Modifier.height(8.dp))

            // 1. План VS Факт
            ChartOptionItem(
                emoji = "📊",
                title = "План VS Факт",
                subtitle = "Что вы планировали и что получилось",
                onClick = { onChartSelected("plan_vs_fact") }
            )

            // 2. Категории
            ChartOptionItem(
                emoji = "🍩",
                title = "Категории",
                subtitle = "Список категорий с наибольшими тратами",
                onClick = { onChartSelected("categories_dashboard") }
            )

            // 3. Цели
            ChartOptionItem(
                emoji = "🎯",
                title = "Цели",
                subtitle = "Что получилось достичь и что перенесем на следующий месяц",
                onClick = { onChartSelected("goals") }
            )
        }
    }
}

@Composable
fun ChartOptionItem(
    emoji: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Иконка (Эмодзи на цветном фоне)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Текст
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.Black
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                lineHeight = 18.sp // Немного увеличиваем интерлиньяж для читаемости длинных подписей
            )
        }

        // Стрелочка
        Text("›", fontSize = 24.sp, color = Color.Gray)
    }
}