package com.tbank.smartbudget.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
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
fun BasicSearchBar(
    searchText: String, // Текущее состояние текста
    onSearchTextChange: (String) -> Unit, // Функция для обновления состояния
    modifier: Modifier = Modifier.Companion,
    backgroundColor: Color = Color(0xFFE0E0E0) // Устанавливаем разумное значение по умолчанию
) {
    // Стиль текста (для value и placeholder)
    val textStyle = TextStyle(
        color = Color.Companion.Black,
        fontSize = 15.sp
    )

    Box(
        modifier = modifier // Принимаем модификатор от родителя
            .height(35.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Companion.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.Companion.CenterVertically,
            modifier = Modifier.Companion.fillMaxSize()
        ) {
            // Иконка
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Поиск",
                tint = Color.Companion.DarkGray.copy(alpha = 0.6f),
                modifier = Modifier.Companion
                    .size(30.dp)
                    .padding(end = 8.dp)
            )

            // Основное поле ввода
            // Внимание: мы оставляем BasicTextField пустым и некликабельным,
            // чтобы он просто показывал placeholder и не активировал клавиатуру
            BasicTextField(
                value = searchText,
                onValueChange = onSearchTextChange, // Используем функцию, переданную снаружи
                singleLine = true,
                textStyle = textStyle,
                modifier = Modifier.Companion.weight(1f),
                enabled = false, // Отключаем ввод

                // Плейсхолдер
                decorationBox = { innerTextField ->
                    Box {
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Поиск",
                                style = textStyle.copy(color = Color.Companion.DarkGray.copy(alpha = 0.6f)),
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}