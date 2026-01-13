package com.tbank.smartbudget.presentation.ui.budget_dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.presentation.ui.budget_dashboard.plan_vs_fact.BarChartComparison
import com.tbank.smartbudget.presentation.ui.budget_dashboard.plan_vs_fact.FactColorCategory
import com.tbank.smartbudget.presentation.ui.budget_dashboard.plan_vs_fact.PlanColorCategory
import com.tbank.smartbudget.presentation.ui.budget_dashboard.plan_vs_fact.PlanVsFactCategoryUi

@Composable
fun CategoryItem(category: PlanVsFactCategoryUi) {
    // Получаем числовые значения из строк для построения графиков
    val planVal = category.planAmount.replace(Regex("[^0-9]"), "").toDoubleOrNull() ?: 0.0
    val factVal = category.factAmount.replace(Regex("[^0-9]"), "").toDoubleOrNull() ?: 0.0

    Row(
        modifier = Modifier.Companion.fillMaxWidth().height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.Companion.CenterVertically,
            modifier = Modifier.Companion.weight(1f)
        ) {
            // Иконка
            Box(
                modifier = Modifier.Companion
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(category.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Companion.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart, // Заглушка
                    contentDescription = null,
                    tint = category.color,
                    modifier = Modifier.Companion.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.Companion.width(12.dp))

            // Название и суммы
            Column {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Companion.SemiBold),
                    color = Color.Companion.Black
                )
                Text(
                    text = "${category.factAmount} / ${category.planAmount}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Companion.Gray
                )
            }
        }

        // Мини-график (столбцы) справа
        Box(
            modifier = Modifier.Companion
                .width(40.dp)
                .height(40.dp),
            contentAlignment = Alignment.Companion.BottomCenter
        ) {
            BarChartComparison(
                planValue = planVal,
                factValue = factVal,
                diffLabel = "",
                isSmall = true,
                listOfColors = listOf(PlanColorCategory, FactColorCategory)
            )
        }
    }
}