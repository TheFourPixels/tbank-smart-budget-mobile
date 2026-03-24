package com.tbank.smartbudget.feature.budget_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.tbank.smartbudget.core.ui.common.DetailsCard
import com.tbank.smartbudget.feature.budget_details.components.InfoRow
import com.tbank.smartbudget.feature.budget_details.components.SettingSwitchRow
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDetailsScreen(
    onNavigateBack: () -> Unit,
    onEditClick: () -> Unit,
    onCalculationsClick: () -> Unit = {},
    viewModel: BudgetDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val density = LocalDensity.current
    val lifecycleOwner = LocalLifecycleOwner.current

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
                // Фон
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

                // Контент
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Шапка
                    Column(modifier = Modifier.padding(horizontal = 18.dp)) {
                        Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            modifier = Modifier.padding(start = 23.dp),
                            text = "Бюджет “${state.budgetName}”",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W700),
                            color = Color.White, lineHeight = 23.sp
                        )
                        Text(
                            modifier = Modifier.padding(start = 23.dp),
                            text = state.currentBalance,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 28.sp),
                            color = Color.White, lineHeight = 33.sp
                        )
                        Text(
                            modifier = Modifier.padding(start = 23.dp),
                            text = state.periodDescription,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.7f), lineHeight = 23.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onEditClick,
                            modifier = Modifier
                                .width(320.dp)
                                .height(50.dp)
                                .align(Alignment.CenterHorizontally),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Редактировать", fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Карточки
                    Column(modifier = Modifier.padding(horizontal = 18.dp)) {
                        // Карточка 1
                        DetailsCard {
                            Text("По вашему бюджету", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(16.dp))
                            InfoRow("Доход", state.income)
                            Spacer(modifier = Modifier.height(12.dp))
                            InfoRow("Общий лимит расходов", state.expenseLimit)
                            Spacer(modifier = Modifier.height(12.dp))
                            InfoRow("Свободные средства", state.freeFunds)
                            Spacer(modifier = Modifier.height(24.dp))

                            // Кнопка "Посмотреть расчеты"
                            Button(
                                onClick = onCalculationsClick,
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFDD2D), contentColor = Color.Black),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Посмотреть расчеты", fontSize = 16.sp, style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Остальные карточки...
                        DetailsCard {
                            Text("Лимит не установлен", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Text("Можно тратить всю сумму с бюджета", style = MaterialTheme.typography.bodyMedium, color = Color.Black, modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))
                            Button(
                                onClick = { /* TODO */ },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5), contentColor = SmartBudgetTheme.colors.blue),
                                shape = RoundedCornerShape(12.dp), elevation = ButtonDefaults.buttonElevation(0.dp)
                            ) {
                                Text("Установить лимит", fontSize = 16.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        DetailsCard {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Привязана к счету", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Text("Изменить", color = SmartBudgetTheme.colors.blue, fontSize = 14.sp, modifier = Modifier.clickable { /* TODO */ })
                            }
                            Spacer(modifier = Modifier.height(25.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(SmartBudgetTheme.colors.blue), contentAlignment = Alignment.Center) {
                                    Text("🛒", fontSize = 20.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(state.linkedAccountBalance, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(state.linkedAccountName, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        DetailsCard {
                            Text("Настройки", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(16.dp))
                            SettingSwitchRow(title = "Оповещение\nо превышении лимита", checked = state.isLimitNotificationEnabled, onCheckedChange = viewModel::onLimitNotificationToggle)
                            Spacer(modifier = Modifier.height(16.dp))
                            SettingSwitchRow(title = "Уведомления\nоб операциях", checked = state.isOperationNotificationEnabled, onCheckedChange = viewModel::onOperationNotificationToggle)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}