package com.tbank.smartbudget

import com.tbank.smartbudget.core.network.remote.api.GoalApi
import com.tbank.smartbudget.core.network.remote.api.TransactionApi
import com.tbank.smartbudget.core.network.remote.api.UnauthorizedApi
import com.tbank.smartbudget.core.network.remote.dto.AuthRequest
import com.tbank.smartbudget.core.network.remote.dto.CreateGoalRequest
import com.tbank.smartbudget.core.network.remote.dto.CreateTransactionRequest
import com.tbank.smartbudget.core.network.remote.dto.RegisterRequest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Тест для заполнения базы данных демонстрационными данными.
 * Запускать вручную при необходимости.
 */
class DataPopulationTest {

    private lateinit var retrofit: Retrofit
    private lateinit var authApi: UnauthorizedApi
    private lateinit var transactionApi: TransactionApi
    private lateinit var goalApi: GoalApi

    private val baseUrl = "http://192.168.0.104:8080/" // Замените на актуальный URL

    @Before
    fun setup() {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()
        val client = OkHttpClient.Builder().build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        authApi = retrofit.create(UnauthorizedApi::class.java)
        transactionApi = retrofit.create(TransactionApi::class.java)
        goalApi = retrofit.create(GoalApi::class.java)
    }

    @Test
    fun populateData() = runBlocking {
        println("Starting data population...")

        // 1. Регистрация или Логин
        val email = "test@example.com"
        val password = "password123"
        
        val token = try {
            val loginResp = authApi.login(AuthRequest(email, password))
            loginResp.token
        } catch (e: Exception) {
            val regResp = authApi.register(RegisterRequest(email, password, "Test User"))
            regResp.token
        }
        
        println("Logged in with token: $token")
        
        // Пересоздаем API с токеном (упрощенно для теста)
        val authenticatedClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
            
        val authRetrofit = retrofit.newBuilder().client(authenticatedClient).build()
        val authTransactionApi = authRetrofit.create(TransactionApi::class.java)
        val authGoalApi = authRetrofit.create(GoalApi::class.java)

        // 2. Создание транзакций
        val categories = listOf(1L, 2L, 3L, 4L, 5L) // Предположим, что ID категорий 1-5 существуют
        val merchants = listOf("Пятерочка", "Лукойл", "Starbucks", "Яндекс.Такси", "Wildberries")
        
        repeat(10) { i ->
            val request = CreateTransactionRequest(
                transactionTime = LocalDateTime.now().minusDays(i.toLong()).format(DateTimeFormatter.ISO_DATE_TIME),
                amount = (100..5000).random().toDouble(),
                type = "EXPENSE",
                merchant = merchants.random(),
                categoryId = categories.random(),
                description = "Demo transaction $i"
            )
            try {
                authTransactionApi.createTransaction(request)
                println("Created transaction $i")
            } catch (e: Exception) {
                println("Failed to create transaction $i: ${e.message}")
            }
        }

        // 3. Создание целей
        val goalNames = listOf("Отпуск", "Новый ноутбук", "Подушка безопасности")
        goalNames.forEach { name ->
            val request = CreateGoalRequest(
                name = name,
                targetAmount = (50000..200000).random().toDouble(),
                deadline = "2026-12-31"
            )
            try {
                authGoalApi.create(request)
                println("Created goal $name")
            } catch (e: Exception) {
                println("Failed to create goal $name: ${e.message}")
            }
        }

        println("Data population finished.")
    }
}
