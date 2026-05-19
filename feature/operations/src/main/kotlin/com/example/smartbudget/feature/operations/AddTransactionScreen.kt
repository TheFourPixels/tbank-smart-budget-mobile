package com.example.smartbudget.feature.operations

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tbank.smartbudget.core.ui.common.CategoryIconPlaceholder
import com.tbank.smartbudget.core.ui.common.DetailsCard
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.data.domain.model.TransactionType
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private fun formatMoney(amount: Double): String {
    return "%,.0f ₽".format(amount).replace(',', ' ')
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: AddTransactionViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSelectedCategories: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )
    val timePickerState = rememberTimePickerState(
        initialHour = state.date.hour,
        initialMinute = state.date.minute
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AddTransactionEffect.NavigateBack -> onNavigateBack()
                AddTransactionEffect.NavigateToSelectedCategories -> onNavigateToSelectedCategories()
                is AddTransactionEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                        viewModel.onIntent(AddTransactionIntent.OnDateChanged(
                            state.date.withYear(selectedDate.year).withMonth(selectedDate.monthValue).withDayOfMonth(selectedDate.dayOfMonth)
                        ))
                        showTimePicker = true
                    }
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Отмена") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showTimePicker = false
                    viewModel.onIntent(AddTransactionIntent.OnDateChanged(
                        state.date.withHour(timePickerState.hour).withMinute(timePickerState.minute)
                    ))
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("Отмена") } },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { viewModel.onIntent(AddTransactionIntent.OnBackClicked) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = { viewModel.onIntent(AddTransactionIntent.OnSaveClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD600), // Yellow from image
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !state.isSaving
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Black)
                    } else {
                        Text("Добавить транзакцию", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Expense/Income Toggle
            TransactionTypeToggle(
                selectedType = state.type,
                onTypeSelected = { viewModel.onIntent(AddTransactionIntent.OnTypeChanged(it)) }
            )

            Spacer(Modifier.height(32.dp))

            // Amount Input
            AmountInput(
                amount = state.amount,
                onAmountChanged = { viewModel.onIntent(AddTransactionIntent.OnAmountChanged(it)) }
            )

            Spacer(Modifier.height(32.dp))

            // Category Section
            DetailsCard {
                Column {
                    Text(
                        "Категория",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        fontSize = 20.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    
                    state.categories.forEach { category ->
                        CategorySelectRow(
                            name = category.categoryName,
                            limit = "Остаток: ${formatMoney(category.limitValue)}",
                            color = Color(category.color),
                            isSelected = state.selectedCategoryId == category.categoryId.value,
                            onClick = { viewModel.onIntent(AddTransactionIntent.OnCategorySelected(category.categoryId.value)) }
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    Spacer(Modifier.height(8.dp))
                    TextButton(
                        onClick = { viewModel.onIntent(AddTransactionIntent.OnAddCategoryClicked) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Добавить категорию", color = SmartBudgetTheme.colors.blue)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Date Section
            DetailsCard(
                modifier = Modifier.clickable { showDatePicker = true }
            ) {
                Column {
                    Text(
                        "Дата",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        fontSize = 20.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(SmartBudgetTheme.colors.blue)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = state.date.format(DateTimeFormatter.ofPattern("d MMMM, HH:mm", Locale("ru"))),
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Merchant Section
            DetailsCard {
                Column {
                    Text(
                        "Название магазина",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        fontSize = 20.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    BasicTextField(
                        value = state.merchantName,
                        onValueChange = { viewModel.onIntent(AddTransactionIntent.OnMerchantNameChanged(it)) },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (state.merchantName.isEmpty()) {
                                Text("Пятерочка", color = Color.Gray, fontSize = 18.sp)
                            }
                            innerTextField()
                        }
                    )
                }
            }
            
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun TransactionTypeToggle(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF2F2F2))
            .padding(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            val expenseWeight = if (selectedType == TransactionType.EXPENSE) 1f else 1f
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (selectedType == TransactionType.EXPENSE) Color.White else Color.Transparent)
                    .clickable { onTypeSelected(TransactionType.EXPENSE) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Расход",
                    fontWeight = if (selectedType == TransactionType.EXPENSE) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedType == TransactionType.EXPENSE) Color.Black else Color.Gray
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (selectedType == TransactionType.INCOME) Color.White else Color.Transparent)
                    .clickable { onTypeSelected(TransactionType.INCOME) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Доход",
                    fontWeight = if (selectedType == TransactionType.INCOME) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedType == TransactionType.INCOME) Color.Black else Color.Gray
                )
            }
        }
    }
}

@Composable
fun AmountInput(
    amount: String,
    onAmountChanged: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicTextField(
            value = amount,
            onValueChange = { if (it.all { char -> char.isDigit() }) onAmountChanged(it) },
            textStyle = TextStyle(
                fontSize = 64.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.width(IntrinsicSize.Min).widthIn(min = 40.dp),
            decorationBox = { innerTextField ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(contentAlignment = Alignment.Center) {
                        if (amount.isEmpty() || amount == "0") {
                            Text("0", fontSize = 64.sp, fontWeight = FontWeight.Black, color = Color.LightGray)
                        }
                        innerTextField()
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("₽", fontSize = 64.sp, fontWeight = FontWeight.Black)
                }
            }
        )
    }
}

@Composable
fun CategorySelectRow(
    name: String,
    limit: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CategoryIconPlaceholder(
            color = color,
            iconRes = 0,
            name = name,
            size = 40.dp,
            iconSize = 20.dp
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(limit, color = Color.Gray, fontSize = 14.sp)
        }
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = SmartBudgetTheme.colors.blue)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddTransactionPreview() {
    SmartBudgetTheme {
    }
}
