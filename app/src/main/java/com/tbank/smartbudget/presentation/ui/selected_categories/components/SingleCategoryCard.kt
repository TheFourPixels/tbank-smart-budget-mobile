package com.tbank.smartbudget.presentation.ui.selected_categories.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.presentation.ui.selected_categories.SelectedCategoryUi

@Composable
fun SingleCategoryCard(category: SelectedCategoryUi) {
    ShadowCardContainer {
        Box(modifier = Modifier.Companion.padding(horizontal = 20.dp, vertical = 16.dp)) {
            CategoryRowItem(category)
        }
    }
}