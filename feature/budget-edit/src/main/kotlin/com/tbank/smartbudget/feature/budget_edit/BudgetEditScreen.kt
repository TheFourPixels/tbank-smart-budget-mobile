package com.tbank.smartbudget.feature.budget_edit

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.feature.budget_edit.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetEditScreen(
    onNavigateBack: () -> Unit,
    onAddCategoryClick: () -> Unit,
    viewModel: BudgetEditViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onIntent(BudgetEditIntent.RefreshCategories)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                BudgetEditEffect.NavigateBack -> onNavigateBack()
                is BudgetEditEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    BudgetEditContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onDeleteClick = { viewModel.onIntent(BudgetEditIntent.OnDeleteClicked) },
        onSaveClick = { viewModel.onIntent(BudgetEditIntent.OnSaveClicked) },
        onClearError = { viewModel.onIntent(BudgetEditIntent.ClearError) },
        onNameChanged = { viewModel.onIntent(BudgetEditIntent.OnNameChanged(it)) },
        onAmountChanged = { viewModel.onIntent(BudgetEditIntent.OnAmountChanged(it)) },
        onToggleLimitType = { viewModel.onIntent(BudgetEditIntent.ToggleGlobalLimitType) },
        onCategoryLimitChanged = { id, value -> viewModel.onIntent(BudgetEditIntent.OnCategoryLimitChanged(id, value)) },
        onCategoryTypeToggle = { id -> viewModel.onIntent(BudgetEditIntent.OnCategoryTypeToggle(id)) },
        onAddCategoryClick = onAddCategoryClick,
        onChangeSourceClick = { /* TODO */ }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BudgetEditContent(
    state: BudgetEditUiState,
    onNavigateBack: () -> Unit,
    onDeleteClick: () -> Unit,
    onSaveClick: () -> Unit,
    onClearError: () -> Unit,
    onNameChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onToggleLimitType: () -> Unit,
    onCategoryLimitChanged: (Long, String) -> Unit,
    onCategoryTypeToggle: (Long) -> Unit,
    onAddCategoryClick: () -> Unit,
    onChangeSourceClick: () -> Unit
) {
    val density = LocalDensity.current
    val gradientHeight = 406.dp
    val gradientHeightPx = with(density) { gradientHeight.toPx() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, "Удалить", tint = Color.White)
                    }
                    IconButton(onClick = onSaveClick) {
                        Icon(Icons.Default.Check, "Сохранить", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->

        if (state.error != null) {
            AlertDialog(
                onDismissRequest = onClearError,
                title = { Text("Ошибка") },
                text = { Text(state.error) },
                confirmButton = {
                    TextButton(onClick = onClearError) {
                        Text("OK")
                    }
                }
            )
        }

        if (state.isLoading || state.isSaving) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = SmartBudgetTheme.colors.blue)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Background Gradient
                    Box(modifier = Modifier.fillMaxWidth().height(gradientHeight)) {
                        Box(modifier = Modifier.fillMaxSize().background(
                            brush = Brush.radialGradient(
                                colors = listOf(SmartBudgetTheme.colors.gradientGreen, SmartBudgetTheme.colors.gradientDarkBlue),
                                center = Offset(Float.POSITIVE_INFINITY, 750.0f),
                                radius = 700f,
                                tileMode = TileMode.Clamp
                            )
                        ))
                        Box(modifier = Modifier.fillMaxSize().background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background.copy(alpha = 0f), MaterialTheme.colorScheme.background),
                                startY = 0.4f * gradientHeightPx, endY = 1.0f * gradientHeightPx
                            )
                        ))
                    }

                    // Content
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))
                        Spacer(modifier = Modifier.height(16.dp))

                        BudgetEditHeader(budgetName = state.budgetName)

                        Spacer(modifier = Modifier.height(24.dp))

                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            BudgetSettingsCard(
                                budgetName = state.budgetName,
                                onNameChanged = onNameChanged,
                                amount = state.amount,
                                onAmountChanged = onAmountChanged,
                                isPercentMode = state.isPercentMode,
                                onToggleLimitType = onToggleLimitType,
                                sourceCardName = state.sourceCardName,
                                sourceCardPan = state.sourceCardPan,
                                sourceCardBalance = state.sourceCardBalance,
                                onChangeSourceClick = onChangeSourceClick
                            )

                            BudgetCategoriesCard(
                                categories = state.categories,
                                onAddCategoryClick = onAddCategoryClick,
                                onCategoryLimitChanged = onCategoryLimitChanged,
                                onCategoryTypeToggle = onCategoryTypeToggle
                            )

                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetEditScreenPreview() {
    SmartBudgetTheme {
        BudgetEditContent(
            state = BudgetEditUiState(),
            onNavigateBack = {},
            onDeleteClick = {},
            onSaveClick = {},
            onClearError = {},
            onNameChanged = {},
            onAmountChanged = {},
            onToggleLimitType = {},
            onCategoryLimitChanged = { _, _ -> },
            onCategoryTypeToggle = {},
            onAddCategoryClick = {},
            onChangeSourceClick = {}
        )
    }
}
