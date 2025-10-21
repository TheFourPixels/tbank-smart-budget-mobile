package com.tbank.smartbudget

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Главный класс Application, помеченный @HiltAndroidApp.
 * Необходим для инициализации Dagger Hilt.
 */
@HiltAndroidApp
class SmartBudgetApp : Application() {
    // Не требует дополнительной логики
}
