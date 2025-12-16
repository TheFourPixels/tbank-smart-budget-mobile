package com.tbank.smartbudget.presentation.ui.budget_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tbank.smartbudget.domain.model.BudgetLimitType
import com.tbank.smartbudget.presentation.ui.common.DetailsCard // Импортируем уже существующий DetailsCard
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.presentation.ui.theme.lightExtendedColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetEditScreen(
    onNavigateBack: () -> Unit,
    onAddCategoryClick: () -> Unit = {},
    viewModel: BudgetEditViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val density = LocalDensity.current

    val gradientHeight = 406.dp
    val gradientHeightPx = with(density) { gradientHeight.toPx() }

    // Логика навигации при успешном сохранении
    LaunchedEffect(state.isSavedSuccess) {
        if (state.isSavedSuccess) {
            viewModel.onSuccessShown() // Сбрасываем флаг
            onNavigateBack() // Возвращаемся назад
        }
    }

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
                actions = {
                    // Кнопка сохранения изменений
                    IconButton(onClick = { viewModel.onSaveClicked() }) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Сохранить",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->

        // Диалог ошибки
        if (state.error != null) {
            AlertDialog(
                onDismissRequest = { viewModel.onErrorShown() },
                title = { Text("Ошибка") },
                text = { Text(state.error ?: "Неизвестная ошибка") },
                confirmButton = {
                    TextButton(onClick = { viewModel.onErrorShown() }) {
                        Text("OK")
                    }
                }
            )
        }

        // Индикатор загрузки
        if (state.isLoading || state.isSaving) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Основной контент
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // --- ФОН (Градиент + Маска) ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(gradientHeight)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(406.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            SmartBudgetTheme.colors.gradientGreen,
                                            SmartBudgetTheme.colors.gradientDarkBlue
                                        ),
                                        center = Offset(Float.POSITIVE_INFINITY, 750.0f),
                                        radius = 700f,
                                        tileMode = TileMode.Clamp
                                    )
                                )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.White.copy(alpha = 0f),
                                            Color.White
                                        ),
                                        startY = 0.4f * gradientHeightPx,
                                        endY = 1.0f * gradientHeightPx
                                    )
                                )
                        )
                    }

                    // --- КОНТЕНТ ---
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Отступ под TopBar
                        Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))
                        Spacer(modifier = Modifier.height(16.dp))

                        // Заголовок
                        Text(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            text = "Бюджет “${state.budgetName}”",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp
                            ),
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Контент с карточками
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // --- КАРТОЧКА 1: Настройки бюджета ---
                            DetailsCard {
                                Text(
                                    "Настройки бюджета",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    fontSize = 20.sp
                                )

                                Spacer(Modifier.height(16.dp))

                                // Выбор периода (Чипсы)
                                LazyRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    itemsIndexed(state.periods) { index, text ->
                                        PeriodChip(
                                            text = text,
                                            isSelected = index == state.selectedPeriodIndex,
                                            onClick = { viewModel.onPeriodSelected(index) }
                                        )
                                    }
                                }

                                Spacer(Modifier.height(16.dp))

                                // Поля ввода (Сумма и Тип)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Поле суммы (СВЯЗАНО С VIEWMODEL)
                                    InputBox(
                                        label = "Сумма вклада",
                                        value = state.amount,
                                        onValueChange = { viewModel.onAmountChanged(it) },
                                        modifier = Modifier.weight(1f)
                                    )
                                    // Поле валюты (Только чтение)
                                    InputBox(
                                        label = state.currency,
                                        value = "Проценты",
                                        onValueChange = {},
                                        modifier = Modifier.weight(1f),
                                        isValueSecondary = true,
                                        readOnly = true
                                    )
                                }

                                Spacer(Modifier.height(16.dp))

                                // Текст и ссылка "Изменить"
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "Мы берем ${state.amount} отсюда",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                    Text(
                                        "Изменить",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SmartBudgetTheme.colors.blue,
                                        modifier = Modifier.clickable { /* TODO: Выбор счета */ }
                                    )
                                }

                                Spacer(Modifier.height(12.dp))

                                // Черная карточка счета
                                DarkSourceCard(
                                    amount = state.sourceCardBalance,
                                    cardNumber = state.sourceCardPan,
                                    cardName = state.sourceCardName
                                )
                            }

                            // --- КАРТОЧКА 2: Категории ---
                            DetailsCard {
                                Text(
                                    "Категории",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    fontSize = 20.sp
                                )

                                Spacer(Modifier.height(16.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                    state.categories.forEach { category ->
                                        // СВЯЗАНО С VIEWMODEL
                                        EditCategoryRow(
                                            category = category,
                                            onLimitChange = { viewModel.onCategoryLimitChanged(category.id, it) },
                                            onTypeToggle = { viewModel.onCategoryTypeToggle(category.id) }
                                        )
                                    }
                                }

                                Spacer(Modifier.height(24.dp))

                                // Кнопка "Добавить категорию"
                                Button(
                                    onClick = onAddCategoryClick,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF5F5F5),
                                        contentColor = SmartBudgetTheme.colors.blue
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = ButtonDefaults.buttonElevation(0.dp)
                                ) {
                                    Text("Добавить категорию", fontSize = 16.sp)
                                }
                            }

                            Spacer(Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }
}

// --- КОМПОНЕНТЫ ---

@Composable
fun PeriodChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFFC6FF00) /* Lime Green */ else Color(0xFFF5F5F5)
    val textColor = Color.Black

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50)) // Полностью круглые края
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = textColor
        )
    }
}

@Composable
fun InputBox(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isValueSecondary: Boolean = false,
    readOnly: Boolean = false
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF9F9F9)) // Очень светло-серый фон
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = if (isValueSecondary) Color.Black else Color(0xFF333333)
        )
        Spacer(Modifier.height(4.dp))

        // Поле ввода значения
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            textStyle = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = if (isValueSecondary) Color.Gray else Color.Black,
                fontSize = if (isValueSecondary) 14.sp else 18.sp,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), // Цифровая клавиатура
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.Center) {
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun DarkSourceCard(amount: String, cardNumber: String, cardName: String) {
    Card(
        modifier = Modifier.fillMaxWidth().height(140.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1C)) // Почти черный
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = amount,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                color = Color.White
            )

            Column {
                Text(
                    text = cardNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Text(
                    text = cardName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun EditCategoryRow(
    category: EditCategoryUi,
    onLimitChange: (String) -> Unit,
    onTypeToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Иконка
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(category.color)),
            contentAlignment = Alignment.Center
        ) {
            // Иконка-заглушка (в реале здесь иконка)
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            // Редактирование лимита
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Лимит: ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                // Поле ввода лимита
                BasicTextField(
                    value = category.limitValue,
                    onValueChange = onLimitChange,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.width(80.dp)
                )

                Spacer(Modifier.width(4.dp))

                // Кнопка переключения типа (₽ / %)
                Box(
                    modifier = Modifier
                        .clickable(onClick = onTypeToggle)
                        .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (category.limitType == BudgetLimitType.PERCENT) "%" else "₽",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = SmartBudgetTheme.colors.blue
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetEditScreenPreview() {
    SmartBudgetTheme {
        BudgetEditScreen(onNavigateBack = {})
    }
}