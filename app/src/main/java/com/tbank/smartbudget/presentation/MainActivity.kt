package com.tbank.smartbudget.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            SmartBudgetAppContent()
        }
    }
}

@Composable
fun SmartBudgetAppContent() {
    SmartBudgetTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SmartBudgetNavHost()
        }
    }
}