package com.tbank.smartbudget.feature.budget_tab.components

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
import com.tbank.smartbudget.core.ui.common.BasicSearchBar
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun UserInfoAndSearch(userName: String, onSearchClick: () -> Unit, onProfileClick: () -> Unit) {
    var searchText by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(top = 16.dp)) {
        // Профиль (Аватар + Имя)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
                .clickable (onClick = onProfileClick)
        ) {
            Box(
                modifier = Modifier
                    .size(37.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                // Заглушка для аватара
                Text(
                    text = userName.firstOrNull()?.toString()?.uppercase() ?: "",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(5.dp))
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = userName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W700),
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = "Профиль",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp).padding(start = 4.dp),
            )
        }

        Spacer(Modifier.height(8.dp))

        // 2. Вызов переиспользуемого компонента поиска
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .clickable(onClick = onSearchClick)
        ) {
            BasicSearchBar(
                searchText = searchText,
                onSearchTextChange = { },
                backgroundColor = SmartBudgetTheme.colors.lightGray,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
