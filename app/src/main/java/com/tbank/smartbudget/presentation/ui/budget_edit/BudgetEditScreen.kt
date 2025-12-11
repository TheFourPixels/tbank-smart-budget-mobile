package com.tbank.smartbudget.presentation.ui.budget_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.tbank.smartbudget.presentation.ui.budget_edit.components.DarkSourceCard
import com.tbank.smartbudget.presentation.ui.budget_edit.components.EditCategoryRow
import com.tbank.smartbudget.presentation.ui.budget_edit.components.EditDetailsCard
import com.tbank.smartbudget.presentation.ui.budget_edit.components.InputBox
import com.tbank.smartbudget.presentation.ui.budget_edit.components.PeriodChip
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

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
                // --- ФОН (Градиент + Маска) ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(gradientHeight)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
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
                        EditDetailsCard {
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
                                itemsIndexed(state.periods) { index, text -> // *** ИСПОЛЬЗУЕМ itemsIndexed ***
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
                                // Поле суммы
                                InputBox(
                                    label = "Сумма вклада",
                                    value = "${state.amount} ₽",
                                    modifier = Modifier.weight(1f),
                                )
                                // Поле типа (Рубли/Проценты)
                                InputBox(
                                    label = state.currency,
                                    value = "Проценты", // Заглушка, можно сделать переключателем
                                    modifier = Modifier.weight(1f),
                                    isValueSecondary = true
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
                                    modifier = Modifier.clickable { /* TODO */ }
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
                        EditDetailsCard {
                            Text(
                                "Категории",
                                style = MaterialTheme.typography.titleMedium
                                    .copy(fontWeight = FontWeight.Bold),
                                fontSize = 20.sp
                            )

                            Spacer(Modifier.height(16.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                state.categories.forEach { category ->
                                    EditCategoryRow(category)
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

@Preview(showBackground = true)
@Composable
fun BudgetEditScreenPreview() {
    SmartBudgetTheme {
        BudgetEditScreen(onNavigateBack = {})
    }
}