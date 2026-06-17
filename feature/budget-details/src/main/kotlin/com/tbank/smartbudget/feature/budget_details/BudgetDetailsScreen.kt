package com.tbank.smartbudget.feature.budget_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.feature.budget_details.components.BudgetHeader
import com.tbank.smartbudget.feature.budget_details.components.BudgetSummaryCard
import com.tbank.smartbudget.feature.budget_details.components.LimitCard
import com.tbank.smartbudget.feature.budget_details.components.LinkedAccountCard
import com.tbank.smartbudget.feature.budget_details.components.SettingsCard
import com.tbank.smartbudget.core.ui.common.CalculationsDialog
import com.tbank.smartbudget.core.ui.common.ChartsBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDetailsScreen(
    onNavigateBack: () -> Unit,
    onEditClick: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onSetLimitClick: () -> Unit = {},
    onChangeAccountClick: () -> Unit = {},
    viewModel: BudgetDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showCalculationsDialog by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val lifecycleOwner = LocalLifecycleOwner.current

    if (showCalculationsDialog) {
        CalculationsDialog(
            onDismiss = { showCalculationsDialog = false }
        )
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadBudgetDetails()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BudgetDetailsContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onEditClick = onEditClick,
        onCalculationsClick = onNavigateToDashboard,
        onInfoClick = { showCalculationsDialog = true },
        onSetLimitClick = onSetLimitClick,
        onChangeAccountClick = onChangeAccountClick,
        onLimitNotificationToggle = viewModel::onLimitNotificationToggle,
        onOperationNotificationToggle = viewModel::onOperationNotificationToggle
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BudgetDetailsContent(
    state: BudgetDetailsUiState,
    onNavigateBack: () -> Unit,
    onEditClick: () -> Unit,
    onCalculationsClick: () -> Unit,
    onInfoClick: () -> Unit,
    onSetLimitClick: () -> Unit,
    onChangeAccountClick: () -> Unit,
    onLimitNotificationToggle: (Boolean) -> Unit,
    onOperationNotificationToggle: (Boolean) -> Unit
) {
    val density = LocalDensity.current
    val gradientHeight = 406.dp
    val gradientHeightPx = with(density) { gradientHeight.toPx() }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
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
                            colors = listOf(Color.Transparent, Color.White.copy(alpha = 0f), Color.White),
                            startY = 0.4f * gradientHeightPx, endY = 1.0f * gradientHeightPx
                        )
                    ))
                }

                // Content
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))

                    BudgetHeader(
                        budgetName = state.budgetName,
                        currentBalance = state.currentBalance,
                        periodDescription = state.periodDescription,
                        onEditClick = onEditClick
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Column(
                        modifier = Modifier.padding(horizontal = 18.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        BudgetSummaryCard(
                            income = state.income,
                            expenseLimit = state.expenseLimit,
                            freeFunds = state.freeFunds,
                            onCalculationsClick = onCalculationsClick,
                            onInfoClick = onInfoClick
                        )

                        LimitCard(
                            onSetLimitClick = onSetLimitClick
                        )

                        LinkedAccountCard(
                            accountName = state.linkedAccountName,
                            accountBalance = state.linkedAccountBalance,
                            onChangeClick = onChangeAccountClick
                        )

                        SettingsCard(
                            isLimitNotificationEnabled = state.isLimitNotificationEnabled,
                            onLimitNotificationToggle = onLimitNotificationToggle,
                            isOperationNotificationEnabled = state.isOperationNotificationEnabled,
                            onOperationNotificationToggle = onOperationNotificationToggle
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetDetailsScreenPreview() {
    SmartBudgetTheme {
        BudgetDetailsContent(
            state = BudgetDetailsUiState(),
            onNavigateBack = {},
            onEditClick = {},
            onCalculationsClick = {},
            onInfoClick = {},
            onSetLimitClick = {},
            onChangeAccountClick = {},
            onLimitNotificationToggle = {},
            onOperationNotificationToggle = {}
        )
    }
}
