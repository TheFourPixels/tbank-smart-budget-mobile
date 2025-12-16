package com.tbank.smartbudget.presentation.ui.budget_edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.domain.model.BudgetLimitType
import com.tbank.smartbudget.presentation.ui.budget_edit.EditCategoryUi
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

@Composable
fun EditCategoryRow(
    category: EditCategoryUi,
    onLimitChange: (String) -> Unit, // Параметр добавлен
    onTypeToggle: () -> Unit // Параметр добавлен
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Иконка
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(category.color)),
            contentAlignment = Alignment.Center
        ) {
            // Иконка-заглушка
            Icon(
                // В реальности здесь будет category.iconRes
                imageVector = Icons.Default.Check, // TODO: Заменить на иконку категории
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            // Редактирование лимита
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Лимит: ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                // Поле ввода лимита
                BasicTextField(
                    value = category.limitValue,
                    onValueChange = onLimitChange,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.width(80.dp)
                )

                Spacer(Modifier.width(4.dp))

                // Кнопка переключения типа (₽ / %)
                Box(
                    modifier = Modifier
                        .clickable(onClick = onTypeToggle)
                        .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (category.limitType == BudgetLimitType.PERCENT) "%" else "₽",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = SmartBudgetTheme.colors.blue
                    )
                }
            }
        }
    }
}