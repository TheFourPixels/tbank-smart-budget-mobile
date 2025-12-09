package com.tbank.smartbudget.presentation.ui.category_search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchInput(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier.Companion,
    focusRequester: FocusRequester
) {
    val textStyle = TextStyle(color = Color.Companion.Black, fontSize = 15.sp)
    val backgroundColor = Color(0xFFE0E0E0)

    Box(
        modifier = modifier
            .padding(start = 16.dp)
            .height(35.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Companion.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.Companion.CenterVertically,
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Поиск",
                tint = Color.Companion.DarkGray.copy(alpha = 0.6f),
                modifier = Modifier.Companion.size(30.dp).padding(end = 8.dp)
            )

            BasicTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                singleLine = true,
                textStyle = textStyle,
                modifier = Modifier.Companion
                    .weight(1f)
                    .focusRequester(focusRequester), // *** Применяем FocusRequester к полю ввода ***
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

            if (searchText.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = "Очистить",
                    tint = Color.Companion.DarkGray.copy(alpha = 0.6f),
                    modifier = Modifier.Companion
                        .size(24.dp)
                        .clickable(onClick = onClearClick)
                        .padding(start = 4.dp)
                )
            }
        }
    }
}