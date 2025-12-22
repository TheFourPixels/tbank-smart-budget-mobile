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
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.tbank.smartbudget.domain.model.BudgetLimitType
import com.tbank.smartbudget.presentation.ui.common.DetailsCard
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.presentation.ui.budget_edit.components.DarkSourceCard
import com.tbank.smartbudget.presentation.ui.budget_edit.components.EditCategoryRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetEditScreen(
    onNavigateBack: () -> Unit,
    onAddCategoryClick: () -> Unit = {},
    viewModel: BudgetEditViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val density = LocalDensity.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // --- ОБНОВЛЕНИЕ ДАННЫХ ПРИ ВОЗВРАТЕ ---
    // Когда экран становится RESUMED (например, вернулись с выбора категорий),
    // обновляем список, чтобы подтянуть новые добавленные категории.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshCategories()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val gradientHeight = 406.dp
    val gradientHeightPx = with(density) { gradientHeight.toPx() }

    LaunchedEffect(state.isSavedSuccess) {
        if (state.isSavedSuccess) {
            viewModel.onSuccessShown()
            onNavigateBack()
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
                    IconButton(onClick = { viewModel.onSaveClicked() }) {
                        Icon(Icons.Default.Check, contentDescription = "Сохранить", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->

        if (state.error != null) {
            AlertDialog(
                onDismissRequest = { viewModel.onErrorShown() },
                title = { Text("Ошибка") },
                text = { Text(state.error ?: "Неизвестная ошибка") },
                confirmButton = { TextButton(onClick = { viewModel.onErrorShown() }) { Text("OK") } }
            )
        }

        if (state.isLoading || state.isSaving) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // --- ФОН ---
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

                    // --- КОНТЕНТ ---
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            text = "Бюджет “${state.budgetName}”",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 28.sp),
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // --- КАРТОЧКА 1: Настройки ---
                            DetailsCard {
                                Text("Настройки бюджета", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), fontSize = 20.sp)
                                Spacer(Modifier.height(16.dp))
                                LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    itemsIndexed(state.periods) { index, text ->
                                        PeriodChip(text = text, isSelected = index == state.selectedPeriodIndex, onClick = { viewModel.onPeriodSelected(index) })
                                    }
                                }
                                Spacer(Modifier.height(16.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    InputBox(
                                        label = "Сумма вклада",
                                        value = state.amount,
                                        onValueChange = { viewModel.onAmountChanged(it) },
                                        modifier = Modifier.weight(1f)
                                    )
                                    UnitSwitchBox(
                                        isPercentMode = state.isPercentMode,
                                        onToggle = { viewModel.onGlobalLimitTypeToggle() },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Spacer(Modifier.height(16.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Мы берем ${state.amount} отсюда", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    Text("Изменить", style = MaterialTheme.typography.bodySmall, color = SmartBudgetTheme.colors.blue, modifier = Modifier.clickable { })
                                }
                                Spacer(Modifier.height(12.dp))
                                DarkSourceCard(state.sourceCardBalance, state.sourceCardPan, state.sourceCardName)
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
                                        EditCategoryRow(
                                            category = category,
                                            onLimitChange = { viewModel.onCategoryLimitChanged(category.id, it) },
                                            onTypeToggle = { viewModel.onCategoryTypeToggle(category.id) }
                                        )
                                    }
                                }

                                Spacer(Modifier.height(24.dp))

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

// Повторное определение компонентов для полноты файла
@Composable
fun PeriodChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFFC6FF00) else Color(0xFFF5F5F5)
    Box(
        modifier = Modifier.clip(RoundedCornerShape(50)).background(backgroundColor)
            .clickable(onClick = onClick).padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium), color = Color.Black)
    }
}

@Composable
fun UnitSwitchBox(isPercentMode: Boolean, onToggle: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(16.dp)).background(Color(0xFFF9F9F9))
            .clickable(onClick = onToggle).padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Единицы", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = Color.Black)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("₽", style = MaterialTheme.typography.titleMedium.copy(fontWeight = if (!isPercentMode) FontWeight.Bold else FontWeight.Normal, color = if (!isPercentMode) Color.Black else Color.Gray, fontSize = 16.sp))
            Text(" / ", style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray, fontSize = 16.sp))
            Text("%", style = MaterialTheme.typography.titleMedium.copy(fontWeight = if (isPercentMode) FontWeight.Bold else FontWeight.Normal, color = if (isPercentMode) Color.Black else Color.Gray, fontSize = 16.sp))
        }
    }
}

@Composable
fun InputBox(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(16.dp)).background(Color(0xFFF9F9F9)).padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = Color(0xFF333333))
        Spacer(Modifier.height(4.dp))
        BasicTextField(
            value = value, onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp, textAlign = TextAlign.Center),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true,
            modifier = Modifier.fillMaxWidth(), decorationBox = { Box(contentAlignment = Alignment.Center) { it() } }
        )
    }
}