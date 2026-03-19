package com.tbank.smartbudget.presentation.ui.budget_dashboard.categories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.tbank.smartbudget.presentation.ui.budget_dashboard.components.CategoriesDonutChart
import com.tbank.smartbudget.presentation.ui.budget_dashboard.components.CategoriesHorizontalBarChartPercent
import com.tbank.smartbudget.presentation.ui.budget_dashboard.components.CategoriesHorizontalBarChartRubles
import com.tbank.smartbudget.presentation.ui.common.DetailsCard
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

// Цвета
private val LabelYellow = Color(0xFFFFD600)

@Composable
fun CategoriesDashboardScreen(
    onNavigateBack: () -> Unit,
    viewModel: CategoriesDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    CategoriesDashboardContent(
        state = state,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CategoriesDashboardContent(
    state: CategoriesDashboardUiState,
    onNavigateBack: () -> Unit
) {
    val density = LocalDensity.current
    val gradientHeight = 500.dp
    val gradientHeightPx = with(density) { gradientHeight.toPx() }

    // Состояние видимости графиков во второй карточке
    var isExpensesChartVisible by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { },
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
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                // --- ФОН (Градиент) ---
                Box(modifier = Modifier.fillMaxWidth().height(gradientHeight)) {
                    Box(modifier = Modifier.fillMaxSize().background(
                        brush = Brush.radialGradient(
                            colors = listOf(SmartBudgetTheme.colors.gradientViolet, SmartBudgetTheme.colors.gradientDarkViolet),
                            center = Offset(Float.POSITIVE_INFINITY, 750.0f),
                            radius = 900f,
                            tileMode = TileMode.Clamp
                        )
                    ))
                    // Плавный переход в белый
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
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))

                    // Заголовок
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Категории",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Тут мы посчитали, в каких категориях \nВы потратили больше всего",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp, lineHeight = 20.sp),
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    // --- КАРТОЧКА 1: Самые популярные категории (Круговая диаграмма) ---
                    DetailsCard {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Самые популярные категории",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.align(Alignment.CenterHorizontally  )
                            )
                            Text(
                                text = "Круговая диаграмма",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(24.dp))

                            Box(contentAlignment = Alignment.Center) {
                                // Диаграмма
                                CategoriesDonutChart(
                                    categories = state.categories,
                                    modifier = Modifier.size(155.dp)
                                )
                                // Текст в центре
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Всего",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = state.totalSpent,
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        color = Color.Black
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Легенда в столбик
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                state.categories.forEach { item ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(item.color)
                                        )

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Text(
                                            text = item.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Black.copy(alpha = 0.8f),
                                            modifier = Modifier.weight(1f)
                                        )

                                        Text(
                                            text = "${(item.percent * 100).toInt()}%",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = Color.Black
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- КАРТОЧКА 2: Топ категорий с раскрывающимися графиками ---
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        // Карточка с отступом под кнопку
                        Box(modifier = Modifier.padding(bottom = 28.dp)) {
                            DetailsCard {
                                Column {
                                    Text(
                                        text = "Топ категорий",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))


                                    CategoriesHorizontalBarChartPercent(
                                        categories = state.categories.take(5)
                                    )



                                    AnimatedVisibility(
                                        visible = isExpensesChartVisible,
                                        enter = expandVertically() + fadeIn(),
                                        exit = shrinkVertically() + fadeOut()
                                    ) {
                                        Column {
                                            Spacer(modifier = Modifier.height(24.dp))

                                            // Мини-карточка (серый фон, скругление)
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(16.dp))
                                                    .background(Color(0xFFF9F9F9))
                                                    .padding(16.dp)
                                            ) {
                                                // Дублируем график или показываем расширенный
                                                CategoriesHorizontalBarChartRubles(
                                                    categories = state.categories.take(5)
                                                )
                                            }
                                        }
                                    }

                                    // Отступ внутри карточки для кнопки
                                    Spacer(modifier = Modifier.height(32.dp))
                                }
                            }
                        }

                        // Желтая кнопка
                        Button(
                            onClick = { isExpensesChartVisible = !isExpensesChartVisible },
                            modifier = Modifier
                                .width(300.dp)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LabelYellow,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(16.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                        ) {
                            Text(
                                text = if (isExpensesChartVisible) "Скрыть" else "Потраченные деньги",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    /*// --- КАРТОЧКА 3: Динамика (График) ---
                    DetailsCard {
                        Column {
                            Text(
                                text = "Динамика",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(24.dp))

                            Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                                SimpleLineChart(
                                    dataPoints = state.historyData,
                                    lineColor = SmartBudgetTheme.colors.blue
                                )
                            }
                        }
                    }
*/
                }
            }
        }
    }
}

// --- Компоненты ---

@Preview(showBackground = true, heightDp = 1000)
@Composable
fun CategoriesDashboardPreview() {
    SmartBudgetTheme {
        CategoriesDashboardContent(
            state = CategoriesDashboardUiState(
                totalSpent = "33 200 ₽",
                categories = listOf(
                    CategoryDashboardItem(1, "Продукты", "15 000 ₽", 15000.0, Color(0xFF43A047), 0.45f),
                    CategoryDashboardItem(2, "Транспорт", "5 000 ₽", 5000.0, Color(0xFF1E88E5), 0.15f),
                    CategoryDashboardItem(3, "Кафе", "4 500 ₽", 4500.0, Color(0xFFFF7043), 0.13f)
                ),
                historyData = listOf(10f, 20f, 15f, 30f, 25f, 40f)
            ),
            onNavigateBack = {}
        )
    }
}