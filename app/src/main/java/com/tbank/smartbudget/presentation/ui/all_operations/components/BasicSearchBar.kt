//package com.tbank.smartbudget.presentation.ui.all_operations.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Cancel
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun BasicSearchBar(
//    searchText: String,
//    onSearchTextChange: (String) -> Unit,
//    modifier: Modifier = Modifier.Companion,
//    backgroundColor: Color = Color(0xFFF5F5F5) // Светло-серый фон
//) {
//    val textStyle = TextStyle(
//        color = Color.Companion.Black,
//        fontSize = 16.sp
//    )
//
//    Box(
//        modifier = modifier
//            .height(44.dp)
//            .background(backgroundColor, RoundedCornerShape(12.dp))
//            .padding(horizontal = 12.dp),
//        contentAlignment = Alignment.Companion.CenterStart
//    ) {
//        Row(
//            verticalAlignment = Alignment.Companion.CenterVertically,
//            modifier = Modifier.Companion.fillMaxSize()
//        ) {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "Поиск",
//                tint = Color.Companion.Gray,
//                modifier = Modifier.Companion.size(24.dp)
//            )
//
//            Spacer(Modifier.Companion.width(8.dp))
//
//            BasicTextField(
//                value = searchText,
//                onValueChange = onSearchTextChange,
//                singleLine = true,
//                textStyle = textStyle,
//                modifier = Modifier.Companion.weight(1f),
//                decorationBox = { innerTextField ->
//                    Box {
//                        if (searchText.isEmpty()) {
//                            Text(
//                                text = "Поиск",
//                                style = textStyle.copy(color = Color.Companion.Gray)
//                            )
//                        }
//                        innerTextField()
//                    }
//                }
//            )
//
//            if (searchText.isNotEmpty()) {
//                Icon(
//                    imageVector = Icons.Filled.Cancel,
//                    contentDescription = "Очистить",
//                    tint = Color.Companion.Gray,
//                    modifier = Modifier.Companion
//                        .size(20.dp)
//                        .clickable { onSearchTextChange("") }
//                )
//            }
//        }
//    }
//}