package com.example.smartbudget.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartbudget.feature.dashboard.components.BudgetLineChart
import com.example.smartbudget.feature.dashboard.components.ChartsBottomSheet
import com.tbank.smartbudget.core.ui.common.CategoryIconPlaceholder
import com.tbank.smartbudget.core.ui.common.DetailsCard
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.data.domain.model.Goal
import com.tbank.smartbudget.data.domain.model.Transaction
import com.tbank.smartbudget.data.domain.model.TransactionType

@Composable
fun BudgetDashboardScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPlanVsFact: () -> Unit,
    onNavigateToCategoriesDashboard: () -> Unit,
    viewModel: BudgetDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BudgetDashboardContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onNavigateToPlanVsFact = onNavigateToPlanVsFact,
        onNavigateToCategoriesDashboard = onNavigateToCategoriesDashboard
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDashboardContent(
    state: BudgetDashboardUiState,
    onNavigateBack: () -> Unit,
    onNavigateToPlanVsFact: () -> Unit,
    onNavigateToCategoriesDashboard: () -> Unit,
) {
    var showChartsSheet by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val backgroundColor = MaterialTheme.colorScheme.background

    if (showChartsSheet) {
        ChartsBottomSheet(
            onDismiss = { showChartsSheet = false },
            onChartSelected = { chartType ->
                showChartsSheet = false
                if (chartType == "plan_vs_fact") {
                    onNavigateToPlanVsFact()
                }
                if (chartType == "categories_dashboard") {
                    onNavigateToCategoriesDashboard()
                }
            }
        )
    }

    val gradientHeight = 500.dp
    val gradientHeightPx = with(density) { gradientHeight.toPx() }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { showChartsSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Диаграммы",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // --- ФОН (Градиент) ---
                    Box(modifier = Modifier.fillMaxWidth().height(gradientHeight)) {
                        Box(modifier = Modifier.fillMaxSize().background(
                            brush = Brush.radialGradient(
                                colors = listOf(SmartBudgetTheme.colors.gradientYellow, SmartBudgetTheme.colors.gradientBlue),
                                center = Offset(Float.POSITIVE_INFINITY, 750.0f),
                                radius = 700f,
                                tileMode = TileMode.Clamp
                            )
                        ))
                        // Плавный переход в фон снизу
                        Box(modifier = Modifier.fillMaxSize().background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, backgroundColor.copy(alpha = 0f), backgroundColor),
                                startY = 0.6f * gradientHeightPx, endY = 1.0f * gradientHeightPx
                            )
                        ))
                    }

                    // --- КОНТЕНТ ---
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))

                        // --- ГРАФИК (Hero section) ---
                        Column {
                            // Крупный текст с оставшейся суммой
                            Text(
                                text = "Денег осталось " + "\n" +
                                        state.remainingAmount,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Spacer(Modifier.height(4.dp))
                            // Описание бюджета
                            Text(
                                text = "анализируем бюджет за ${state.periodDescription}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )

                            Spacer(Modifier.height(24.dp))

                            // Мок-данные для графика
                            val mockChartData = remember {
                                listOf(1.0f, 0.95f, 0.88f, 0.82f, 0.75f, 0.60f, 0.55f, 0.48f, 0.40f, 0.45f)
                            }

                            BudgetLineChart(
                                dataPoints = mockChartData,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                lineColor = Color.White,
                                dotColor = SmartBudgetTheme.colors.gradientYellow
                            )
                        }

                        // Отступ между графиком и карточкой
                        Spacer(Modifier.height(32.dp))

                        // 1. Окно с информацией (Объединенная карточка)
                        DetailsCard {
                            Text(
                                "Сводка по бюджету", 
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(Modifier.height(16.dp))

                            SummaryRow("Получено", state.totalIncome, SmartBudgetTheme.colors.gradientGreen)
                            SummaryRow("Траты (лимит)", state.totalLimit, MaterialTheme.colorScheme.onSurface)
                            SummaryRow("Потрачено", state.totalSpent, Color(state.progressColor))
                            SummaryRow("Осталось", state.remainingAmount, MaterialTheme.colorScheme.onSurface)
                            
                            Spacer(Modifier.height(20.dp))

                            Button(
                                onClick = { /* Действие кнопки */ },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFDD2D), contentColor = Color.Black),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Посмотреть расчеты", fontSize = 16.sp, style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        // 2. Статус (Маленькая карточка)
                        val isOverBudget = state.remainingAmount.contains("-")
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isOverBudget) Color(0xFFFFEBEE).copy(alpha = if (isSystemInDarkTheme()) 0.2f else 1f) 
                                               else Color(0xFFE8F5E9).copy(alpha = if (isSystemInDarkTheme()) 0.2f else 1f)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isOverBudget) "Вы превысили лимит бюджета!" else "Вы идете по плану. Так держать!",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isSystemInDarkTheme()) Color.White else Color.Black
                            )
                        }

                        // 3. Активные цели
                        if (state.activeGoals.isNotEmpty()) {
                            Text(
                                "Активные цели", 
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            state.activeGoals.forEach { goal ->
                                GoalItem(goal)
                            }
                        }

                        // 4. Последние операции
                        if (state.recentTransactions.isNotEmpty()) {
                            Text(
                                "Последние операции", 
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            state.recentTransactions.forEach { transaction ->
                                DashboardTransactionItem(transaction)
                            }
                        }

                        Spacer(Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
        Text(text = value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = valueColor))
    }
}

@Composable
fun GoalItem(goal: Goal) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(goal.name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text("${goal.progressPercent}%", color = SmartBudgetTheme.colors.blue)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { goal.progressPercent / 100f },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = SmartBudgetTheme.colors.blue,
                trackColor = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Накоплено ${goal.savedAmount} ₽ из ${goal.targetAmount} ₽",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun DashboardTransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CategoryIconPlaceholder(
            color = Color(transaction.categoryColor),
            iconRes = 0,
            name = transaction.categoryName,
            size = 40.dp,
            iconSize = 20.dp
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                transaction.merchantName ?: transaction.categoryName, 
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row {
                Text(
                    transaction.categoryName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    " • ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    transaction.date.toLocalDate().toString(),
                    style = MaterialTheme.typography.bodySmall, 
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        val amountPrefix = if (transaction.type == TransactionType.INCOME) "+" else "-"
        val amountColor = if (transaction.type == TransactionType.INCOME) SmartBudgetTheme.colors.gradientGreen else MaterialTheme.colorScheme.onBackground
        Text(
            text = "$amountPrefix ${transaction.amount} ₽",
            fontWeight = FontWeight.Bold,
            color = amountColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetDashboardScreenPreview() {
    SmartBudgetTheme {
        BudgetDashboardContent(
            state = BudgetDashboardUiState(
                totalLimit = "30 000 ₽",
                totalSpent = "12 500 ₽",
                remainingAmount = "17 500 ₽",
                progress = 0.42f,
                progressColor = 0xFF43A047,
                daysLeft = 12,
                dailyBudget = "1 458 ₽",
                periodDescription = "Декабрь"
            ),
            onNavigateBack = {},
            onNavigateToPlanVsFact = {},
        ) {}
    }
}
