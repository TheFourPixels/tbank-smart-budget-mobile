package com.tbank.smartbudget.presentation.ui.selected_categories.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchInputBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    // Используем стиль как в BudgetTab, но без стрелки "Назад" внутри поля
    // Если нужна кнопка назад снаружи, раскомментируйте IconButton ниже
    Row(
        verticalAlignment = Alignment.Companion.CenterVertically,
        modifier = Modifier.Companion.fillMaxWidth()
    ) {
        // Кнопка Назад (опционально, если это не корневой экран)
        /*IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.Black)
        }
        Spacer(Modifier.width(8.dp))*/

        Box(
            modifier = Modifier.Companion
                .height(44.dp)
                .weight(1f)
                .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Companion.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.Companion.CenterVertically,
                modifier = Modifier.Companion.fillMaxSize()
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Companion.Gray)
                Spacer(Modifier.Companion.width(8.dp))
                BasicTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Companion.Black, fontSize = 16.sp),
                    modifier = Modifier.Companion.weight(1f),
                    decorationBox = { innerTextField ->
                        Box {
                            if (searchText.isEmpty()) {
                                Text("Поиск", color = Color.Companion.Gray, fontSize = 16.sp)
                            }
                            innerTextField()
                        }
                    }
                )
                if (searchText.isNotEmpty()) {
                    Icon(
                        Icons.Filled.Cancel,
                        contentDescription = "Очистить",
                        tint = Color.Companion.Gray,
                        modifier = Modifier.Companion.clickable { onSearchTextChange("") }
                    )
                }
            }
        }
    }
}