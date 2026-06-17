package com.example.smartbudget.feature.dashboard.plan_vs_fact

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartbudget.feature.dashboard.components.SummaryCard
import com.tbank.smartbudget.core.ui.common.DetailsCard
import com.tbank.smartbudget.core.ui.common.CalculationsDialog
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme.colors
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tbank.smartbudget.data.domain.model.CategoryId


val PlanColorCategory = Color(0xFF528ECE)
val FactColorCategory = Color(0xFF295C9E)
private val LabelYellow = Color(0xFFFFD600)

@Composable
fun PlanVsFactScreen(
    onNavigateBack: () -> Unit,
    viewModel: PlanVsFactViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PlanVsFactContent(
        state = state,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanVsFactContent(
    state: PlanVsFactUiState,
    onNavigateBack: () -> Unit
) {
    var showCalculationsDialog by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val gradientHeight = 500.dp
    val gradientHeightPx = with(density) { gradientHeight.toPx() }

    if (showCalculationsDialog) {
        CalculationsDialog(
            onDismiss = { showCalculationsDialog = false }
        )
    }

    var selectedCategoryIndex by remember(state.categories) { mutableIntStateOf(0) }
    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }

    val selectedCategory = state.categories.getOrNull(selectedCategoryIndex)

    val PlanColor = colors.gradientViolet
    val FactColor = colors.gradientDarkViolet


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
                            colors = listOf(colors.gradientViolet, colors.gradientDarkViolet),
                            center = Offset(Float.POSITIVE_INFINITY, 750.0f),
                            radius = 900f,
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
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))

                    // 1. Секция Заголовка и Описания
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Заголовок: Жирный и крупный
                        Text(
                            text = "План vs Факт",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.W700
                            ),
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Описание: Маленький серый шрифт
                        Text(
                            text = "Тут учитываются ваши планы\n" +
                                    "и сравниваются с получившимися\n" +
                                    "результатами",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 20.sp,
                                fontSize = 14.sp
                            ),
                            color = Color.LightGray
                        )
                    }

                    // 2. Отступ до карточки
                    Spacer(modifier = Modifier.height(48.dp))

                    // 3. Карточка ОБЩАЯ СВОДКА
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        // Карточка (с отступом снизу, равным половине высоты кнопки)
                        Box(modifier = Modifier.padding(bottom = 28.dp)) {
                            DetailsCard {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                    // Заголовок внутри карточки
                                    Text(
                                        text = "Общие траты",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Ряд План/Факт
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        SummaryCard(
                                            title = "План",
                                            amount = state.totalPlan,
                                            backgroundColor = PlanColor,
                                            modifier = Modifier.weight(1f)
                                        )

                                        SummaryCard(
                                            title = "Факт",
                                            amount = state.totalFact,
                                            backgroundColor = FactColor,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(24.dp))

                                    // График
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        BarChartComparison(
                                            planValue = state.planValue,
                                            factValue = state.factValue,
                                            diffLabel = state.percentageDiffLabel,
                                            listOfColors = listOf(PlanColor, FactColor)
                                        )
                                    }

                                    // Отступ внутри карточки
                                    Spacer(modifier = Modifier.height(32.dp))
                                }
                            }
                        }

                        // Желтая кнопка
                        Button(
                            onClick = { showCalculationsDialog = true },
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
                                text = "Текстовое описание для Вас",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 4. Карточка ПО КАТЕГОРИЯМ (С выбором)
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(modifier = Modifier.padding(bottom = 28.dp)) {
                            DetailsCard {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                    // --- Выбор категории (Овальная рамка по центру) ---
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // Обертка для рамки и клика
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50)) // Овал
                                                .border(
                                                    width = 2.dp,
                                                    color = selectedCategory?.color ?: Color.LightGray,
                                                    shape = RoundedCornerShape(50)
                                                )
                                                .clickable { isCategoryDropdownExpanded = true }
                                                .padding(horizontal = 24.dp, vertical = 8.dp) // Внутренний отступ
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = selectedCategory?.name ?: "Нет категорий",
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 18.sp
                                                    ),
                                                    color = Color.Black
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Icon(
                                                    imageVector = Icons.Default.ArrowDropDown,
                                                    contentDescription = "Выбрать категорию",
                                                    tint = Color.Black,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }

                                        // Выпадающее меню
                                        DropdownMenu(
                                            expanded = isCategoryDropdownExpanded,
                                            onDismissRequest = { isCategoryDropdownExpanded = false },
                                            modifier = Modifier.background(Color.White)
                                        ) {
                                            state.categories.forEachIndexed { index, category ->
                                                DropdownMenuItem(
                                                    text = { Text(category.name) },
                                                    onClick = {
                                                        selectedCategoryIndex = index
                                                        isCategoryDropdownExpanded = false
                                                    },
                                                    leadingIcon = {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(24.dp)
                                                                .clip(CircleShape)
                                                                .background(category.color)
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(24.dp))

                                    if (selectedCategory != null) {
                                        // Ряд План/Факт для выбранной категории
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                            SummaryCard(
                                                title = "План",
                                                amount = selectedCategory.planAmount,
                                                backgroundColor = PlanColorCategory,
                                                modifier = Modifier.weight(1f)
                                            )

                                            SummaryCard(
                                                title = "Факт",
                                                amount = selectedCategory.factAmount,
                                                backgroundColor = FactColorCategory,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(24.dp))

                                        // График для категории
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(220.dp),
                                            contentAlignment = Alignment.BottomCenter
                                        ) {
                                            // Расчет локального процента для лейбла
                                            val pVal = selectedCategory.planAmount.parseMoney()
                                            val fVal = selectedCategory.factAmount.parseMoney()
                                            val diff = if (pVal > 0) ((fVal - pVal) / pVal) * 100 else 0.0
                                            val sign = if (diff > 0) "+" else ""
                                            val catDiffLabel = "$sign%.1f%%".format(diff).replace('.', ',')

                                            BarChartComparison(
                                                planValue = pVal,
                                                factValue = fVal,
                                                diffLabel = catDiffLabel,
                                                listOfColors = listOf(PlanColorCategory, FactColorCategory)
                                            )
                                        }
                                    } else {
                                        Text(
                                            "Выберите категорию или создайте бюджет",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(vertical = 32.dp)
                                        )
                                    }

                                    // Отступ внутри карточки
                                    Spacer(modifier = Modifier.height(32.dp))
                                }
                            }
                        }

                        // Вторая желтая кнопка
                        Button(
                            onClick = { showCalculationsDialog = true },
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
                                text = "Текстовое описание для Вас",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 5. Карточка ЛИНЕЙНЫЙ ГРАФИК
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        // Карточка с отступом под кнопку
                        Box(modifier = Modifier.padding(bottom = 28.dp)) {
                            DetailsCard {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "График расходов за \n ${state.periodName}",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        textAlign = Center
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                    ) {
                                        ExpensesLineChart(
                                            dataPoints = state.expenseHistory,
                                            modifier = Modifier.fillMaxSize(),
                                            lineColor = FactColor,
                                            limitThreshold = state.dailyLimit,
                                            daysInMonth = state.daysInMonth
                                        )
                                    }

                                    // Отступ внутри карточки (для кнопки)
                                    Spacer(modifier = Modifier.height(32.dp))
                                }
                            }
                        }

                        // Третья желтая кнопка для графика расходов
                        Button(
                            onClick = { showCalculationsDialog = true },
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
                                text = "Текстовое описание для Вас",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

// Вспомогательная функция для парсинга сумм из UI строк
private fun String.parseMoney(): Double {
    return this.replace(Regex("[^0-9]"), "").toDoubleOrNull() ?: 0.0
}

@Composable
fun BarChartComparison(
    planValue: Double,
    factValue: Double,
    diffLabel: String,
    listOfColors: List<Color>,
    isSmall: Boolean = false
) {
    // Определяем масштаб графика
    val maxValue = maxOf(planValue, factValue) * 1.2
    val safeMax = if (maxValue == 0.0) 1.0 else maxValue

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        // --- Ось Y (Метки) ---
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val labelCount = 4
            for (i in labelCount downTo 0) {
                val value = (safeMax / labelCount) * i
                val text = "%.0f".format(value)
                Text(
                    text = if (value == 0.0) "0" else text,
                    color = Color(0xFF37474F),
                    fontSize = 12.sp,
                    modifier = Modifier.height(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // --- Область рисования графика ---
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            val heightPx = constraints.maxHeight.toFloat()

            val planHeightRatio = (planValue / safeMax).toFloat()
            val factHeightRatio = (factValue / safeMax).toFloat()

            // 1. Пунктирная линия (Plan Level)
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (planValue > 0) {
                    val yPos = heightPx * (1 - planHeightRatio)

                    drawLine(
                        color = LabelYellow,
                        start = Offset(0f, yPos),
                        end = Offset(size.width, yPos),
                        strokeWidth = (if (isSmall) 2.dp else 3.dp).toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
                    )

                    if (!isSmall) {
                        drawCircle(
                            color = LabelYellow,
                            radius = 5.dp.toPx(),
                            center = Offset(0f, yPos)
                        )
                    }
                }
            }

            // 2. Столбцы
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = if (isSmall) Arrangement.SpaceBetween else Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                val barWidth = if (isSmall) 12.dp else 60.dp
                val radius = if (isSmall) 4.dp else 16.dp

                // Левый столбец (План)
                Box(
                    modifier = Modifier
                        .width(barWidth)
                        .fillMaxHeight(planHeightRatio.coerceAtLeast(0.01f))
                        .clip(RoundedCornerShape(topStart = radius, topEnd = radius))
                        .background(listOfColors.component1())
                )

                if (isSmall) Spacer(Modifier.width(4.dp))

                // Правый столбец (Факт)
                Box(
                    modifier = Modifier
                        .width(barWidth)
                        .fillMaxHeight(factHeightRatio.coerceAtLeast(0.01f))
                        .clip(RoundedCornerShape(topStart = radius, topEnd = radius))
                        .background(listOfColors.component2())
                )
            }

            // 3. Бабл с процентами
            if (!isSmall && planValue > 0) {
                val yPosPlan = heightPx * (1 - planHeightRatio)

                Box(
                    modifier = Modifier
                        .offset(x = 0.dp, y = with(LocalDensity.current) { (yPosPlan).toDp() - 16.dp })
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Spacer(modifier = Modifier.width(60.dp))

                        // Бабл
                        Box(
                            modifier = Modifier
                                .background(LabelYellow, RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = diffLabel,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

// Компонент Линейного Графика Расходов (X=Дни, Y=Траты)
@Composable
fun ExpensesLineChart(
    dataPoints: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color,
    limitThreshold: Float = 0f, // Порог для отображения точки превышения
    daysInMonth: Int = 30
) {
    // Вспомогательная функция для рисования текста
    val textPaint = remember {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 28f // Размер шрифта
            textAlign = Paint.Align.LEFT
            isAntiAlias = true
        }
    }

    if (dataPoints.isEmpty()) return

    val maxValue = maxOf(dataPoints.maxOrNull() ?: 0f, limitThreshold)
    // Добавляем небольшой отступ сверху (20%)
    val yRange = if (maxValue > 0) maxValue * 1.2f else 1f

    // Находим индекс первого превышения лимита
    val firstOverLimitIndex = if (limitThreshold > 0) {
        dataPoints.indexOfFirst { it > limitThreshold }
    } else -1

    Row(modifier = modifier.fillMaxSize()) {
        // --- Ось Y (Суммы) ---
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val labelCount = 4
            for (i in labelCount downTo 0) {
                val value = (yRange / labelCount) * i
                // Форматируем: 1000 -> 1k, иначе просто число
                val text = if (value >= 1000) "%.1fk".format(value / 1000) else "%.0f".format(value)
                Text(
                    text = if (value == 0f) "0" else text,
                    color = Color(0xFF37474F),
                    fontSize = 10.sp,
                    modifier = Modifier.height(16.dp)
                )
            }
            Spacer(Modifier.height(16.dp)) // место под ось X
        }

        Spacer(modifier = Modifier.width(12.dp))

        // --- График ---
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {

            // Область рисования линий
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    // Шаг по оси X
                    val stepX = width / (daysInMonth - 1).coerceAtLeast(1)

                    // Рисуем путь линии
                    val strokePath = Path().apply {
                        val firstY = height - (dataPoints.first() / yRange) * height
                        moveTo(0f, firstY)

                        for (i in 0 until dataPoints.size - 1) {
                            val p1 = dataPoints[i]
                            val p2 = dataPoints[i + 1]

                            val x1 = i * stepX
                            val y1 = height - (p1 / yRange) * height
                            val x2 = (i + 1) * stepX
                            val y2 = height - (p2 / yRange) * height

                            // Кривые Безье для плавности
                            val cx1 = x1 + stepX / 2
                            val cy1 = y1
                            val cx2 = x1 + stepX / 2
                            val cy2 = y2

                            cubicTo(cx1, cy1, cx2, cy2, x2, y2)
                        }
                    }

                    // Отрисовка линии
                    drawPath(
                        path = strokePath,
                        color = lineColor,
                        style = Stroke(
                            width = 3.dp.toPx(),
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )

                    // 1. Линия лимита (дневного)
                    if (limitThreshold > 0) {
                        val yLimit = height - (limitThreshold / yRange) * height
                        drawLine(
                            color = LabelYellow.copy(alpha = 0.6f),
                            start = Offset(0f, yLimit),
                            end = Offset(width, yLimit),
                            strokeWidth = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
                        )
                    }

                    // Заливка градиентом под линией (опционально, для красоты)
                    val fillPath = Path().apply {
                        addPath(strokePath)
                        lineTo(width, height)
                        lineTo(0f, height)
                        close()
                    }

                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                lineColor.copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = height
                        )
                    )

                    // Рисуем точки
                    for (i in dataPoints.indices) {
                        val value = dataPoints[i]
                        val x = i * stepX
                        val y = height - (value / yRange) * height

                        // Если значение выше лимита
                        if (limitThreshold > 0 && value > limitThreshold) {
                            // Желтая точка превышения
                            drawCircle(
                                color = LabelYellow,
                                radius = 6.dp.toPx(),
                                center = Offset(x, y)
                            )
                            
                            // Подпись "Превышение" только для первого случая или значимого пика
                            if (i == firstOverLimitIndex) {
                                drawContext.canvas.nativeCanvas.drawText(
                                    "Превышение",
                                    x + 12.dp.toPx(),
                                    y + 4.dp.toPx(),
                                    textPaint
                                )
                            }
                        } else {
                            // Обычные точки
                            drawCircle(
                                color = Color.White,
                                radius = 4.dp.toPx(),
                                center = Offset(x, y)
                            )
                            drawCircle(
                                color = lineColor,
                                radius = 4.dp.toPx(),
                                center = Offset(x, y),
                                style = Stroke(width = 2.dp.toPx())
                            )
                        }
                    }
                }
            }

            // --- Ось X (Дни) ---
            // Рисуем подписи только для некоторых дней (например, каждые 3-4 дня) чтобы не нагромождать
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp), // Высота под подписи
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Просто распределим несколько меток
                val daysToShow = listOf(1, 5, 10, 15, 20, 25, 30)
                // Для простоты реализации в Row SpaceBetween, просто выведем текстом начало, середину и конец периода
                Text("1", fontSize = 10.sp, color = Color.Gray)
                Text("5", fontSize = 10.sp, color = Color.Gray)
                Text("10", fontSize = 10.sp, color = Color.Gray)
                Text("15", fontSize = 10.sp, color = Color.Gray)
                Text("20", fontSize = 10.sp, color = Color.Gray)
                Text("25", fontSize = 10.sp, color = Color.Gray)
                Text("30", fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PlanVsFactPreview() {
    SmartBudgetTheme {
        PlanVsFactContent(
            state = PlanVsFactUiState(
                totalPlan = "30 000 ₽",
                totalFact = "45 000 ₽",
                planValue = 30000.0,
                factValue = 45000.0,
                percentageDiffLabel = "+50%",
                isLoading = false,
                categories = listOf(
                    PlanVsFactCategoryUi(
                        CategoryId(1),
                        "Продукты",
                        0,
                        Color(0xFF43A047),
                        "20 000 ₽",
                        "15 000 ₽",
                        0.75f,
                        Color(0xFF43A047)
                    ),
                    PlanVsFactCategoryUi(
                        CategoryId(2),
                        "Транспорт",
                        0,
                        Color(0xFF1E88E5),
                        "5 000 ₽",
                        "6 000 ₽",
                        1.2f,
                        Color(0xFFE53935)
                    )
                )
            ),
            onNavigateBack = {}
        )
    }
}