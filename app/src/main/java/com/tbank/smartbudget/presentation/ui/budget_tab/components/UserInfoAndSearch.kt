package com.tbank.smartbudget.presentation.ui.budget_tab.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.presentation.ui.common.BasicSearchBar
import com.tbank.smartbudget.presentation.ui.theme.PrimaryDark
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

@Composable
fun UserInfoAndSearch(userName: String, onSearchClick: () -> Unit) { // Принимаем колбэк
    // 1. Управление состоянием поиска внутри родителя (Hoisting State)
    var searchText by remember { mutableStateOf("") }

    Column(modifier = Modifier.Companion.padding(top = 16.dp)) {
        // Профиль (Аватар + Имя)
        Row(
            verticalAlignment = Alignment.Companion.CenterVertically,
            modifier = Modifier.Companion
                .padding(start = 16.dp)
                .fillMaxWidth()
                .clickable { /* Перейти в профиль */ }
        ) {
            Box(
                modifier = Modifier.Companion
                    .size(37.dp)
                    .clip(CircleShape)
                    .background(PrimaryDark.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Companion.Center
            ) {
                // Заглушка для аватара
                Text(
                    text = userName.first().toString(),
                    color = PrimaryDark,
                    fontWeight = FontWeight.Companion.Bold
                )
            }
            Spacer(Modifier.Companion.width(5.dp))
            Text(
                modifier = Modifier.Companion.padding(start = 5.dp),
                text = userName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Companion.W700),
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = "Профиль",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.Companion.size(24.dp).padding(start = 4.dp),
            )
        }

        Spacer(Modifier.Companion.height(8.dp))

        // 2. Вызов переиспользуемого компонента поиска
        Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .clickable(onClick = onSearchClick) // *** Добавляем клик для перехода ***
        ) {
            BasicSearchBar(
                // В этом контексте searchText используется только для отображения плейсхолдера,
                // так как фактический поиск происходит на CategorySearchScreen.
                searchText = searchText,
                onSearchTextChange = { /* Не делаем ничего, так как переходим на другой экран */ },
                backgroundColor = SmartBudgetTheme.colors.lightGray, // Цвет вашей темы
                modifier = Modifier.Companion.fillMaxWidth()
            )
        }
    }
}