package com.tbank.smartbudget.presentation.ui.budget_dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tbank.smartbudget.presentation.ui.budget_dashboard.components.ChartsBottomSheet
import com.tbank.smartbudget.presentation.ui.common.DetailsCard
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

@Composable
fun BudgetDashboardScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPlanVsFact: () -> Unit, // Добавили колбэк
    onNavigateToCategoriesDashboard: () -> Unit,
    viewModel: BudgetDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    BudgetDashboardContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onNavigateToPlanVsFact = onNavigateToPlanVsFact, // Передаем
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
    // Состояние видимости нижнего экрана (Bottom Sheet)
    var showChartsSheet by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    // Если переменная true, показываем BottomSheet
    if (showChartsSheet) {
        ChartsBottomSheet(
            onDismiss = { showChartsSheet = false },
            onChartSelected = { chartType ->
                showChartsSheet = false
                // Обработка выбора
                if (chartType == "plan_vs_fact") {
                    onNavigateToPlanVsFact()
                }
                // Для других типов пока заглушки
                if (chartType == "categories_dashboard") {
                    onNavigateToCategoriesDashboard()
                }
            }
        )
    }

    // Параметры для градиентного фона
    val gradientHeight = 500.dp
    val gradientHeightPx = with(density) { gradientHeight.toPx() }

    Scaffold(
        containerColor = Color.White,
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
                        // Плавный переход в белый фон снизу
                        Box(modifier = Modifier.fillMaxSize().background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.White.copy(alpha = 0f), Color.White),
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
                                        "${state.remainingAmount}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Spacer(Modifier.height(4.dp))
                            // Описание бюджета
                            Text(
                                text = "анализируем бюджет \"Кубышка\"",
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
                            // Заголовок карточки
                            Text("Сводка по бюджету", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Spacer(Modifier.height(16.dp))

                            // Траты и Лимит
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Было денег", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    Text(
                                        text = state.totalLimit,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Осталось денег", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    Text(
                                        text = state.remainingAmount,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Потрачено", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    Text(
                                        text = state.totalSpent,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Color(state.progressColor))
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Получено", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    Text(
                                        text = "0 ₽", // Заглушка
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = SmartBudgetTheme.colors.gradientGreen)
                                    )
                                }
                            }
                            Spacer(Modifier.height(20.dp))

                            // Кнопка "Посмотреть расчеты"
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
                            colors = CardDefaults.cardColors(containerColor = if (isOverBudget) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isOverBudget) "Вы превысили лимит бюджета!" else "Вы идете по плану. Так держать!",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        }

                        Spacer(Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

// Компонент для рисования ПЛАВНОГО графика
@Composable
fun BudgetLineChart(
    dataPoints: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color,
    dotColor: Color
) {
    Canvas(modifier = modifier) {
        if (dataPoints.isEmpty()) return@Canvas

        val width = size.width
        val height = size.height
        // Шаг по оси X
        val stepX = width / (dataPoints.size - 1).coerceAtLeast(1)

        // 1. Создаем путь для линии (используем cubicTo для плавности)
        val strokePath = Path().apply {
            val startY = height * (1 - dataPoints.first())
            moveTo(0f, startY)

            for (i in 0 until dataPoints.size - 1) {
                val p1 = dataPoints[i]
                val p2 = dataPoints[i + 1]

                val x1 = i * stepX
                val y1 = height * (1 - p1)
                val x2 = (i + 1) * stepX
                val y2 = height * (1 - p2)

                // Контрольные точки для кривой Безье (середина по X)
                val cx1 = x1 + stepX / 2
                val cy1 = y1
                val cx2 = x1 + stepX / 2
                val cy2 = y2

                cubicTo(cx1, cy1, cx2, cy2, x2, y2)
            }
        }

        // Рисуем ТОЛЬКО линию, с градиентом
        drawPath(
            path = strokePath,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    lineColor.copy(alpha = 0.4f),
                    lineColor.copy(alpha = 1.0f)
                ),
                startX = 0f,
                endX = width
            ),
            style = Stroke(
                width = 5.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Рисуем точку только для ПОСЛЕДНЕГО значения
        if (dataPoints.isNotEmpty()) {
            val i = dataPoints.lastIndex
            val p = dataPoints.last()
            val center = Offset(i * stepX, height * (1 - p))

            drawCircle(
                color = Color.White,
                radius = 6.dp.toPx(),
                center = center
            )
            drawCircle(
                color = dotColor,
                radius = 6.dp.toPx(),
                center = center,
                style = Stroke(width = 3.dp.toPx())
            )
        }
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