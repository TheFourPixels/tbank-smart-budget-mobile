package com.tbank.smartbudget.feature.budget_tab.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun WhiteBackgroundContainer(content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = SmartBudgetTheme.colors.shadowColor,
                spotColor = SmartBudgetTheme.colors.shadowColor
            )
            .background(MaterialTheme.colorScheme.surface, shape = shape)
    ) {
        content()
    }
}
