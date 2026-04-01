package com.tbank.smartbudget.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tbank.smartbudget.core.network.remote.api.AuthApi
import com.tbank.smartbudget.core.network.remote.api.BudgetApi
import com.tbank.smartbudget.core.network.remote.api.CategoryApi
import com.tbank.smartbudget.core.network.remote.api.DashboardApi
import com.tbank.smartbudget.core.network.remote.api.GoalApi
import com.tbank.smartbudget.core.network.remote.api.TransactionApi
import com.tbank.smartbudget.core.network.remote.interceptor.AuthInterceptor
import com.tbank.smartbudget.core.network.remote.interceptor.ErrorInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Основной модуль сетевого слоя.
 * Настроен для работы с распределенной микросервисной архитектурой.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://192.168.0.188"
    private const val AUTH_URL = "http://$BASE_URL:8089/"
    private const val BUDGET_URL = "http://$BASE_URL:8081/"
    private const val TRANSACTION_URL = "http://$BASE_URL:8083/"
    private const val GOAL_URL = "http://$BASE_URL:8087/"
    private const val DASHBOARD_URL = "http://$BASE_URL:8088/"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true

    }

    @Provides
    @Singleton
    @PublicClient
    fun providePublicOkHttpClient(
        errorInterceptor: ErrorInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "SmartBudget-Android")
                .header("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(errorInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @Provides
    @Singleton
    @AuthenticatedClient
    fun provideAuthenticatedOkHttpClient(
        authInterceptor: AuthInterceptor,
        errorInterceptor: ErrorInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "SmartBudget-Android")
                .header("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(authInterceptor)
        .addInterceptor(errorInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    // --- Retrofit Builders ---

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(@PublicClient client: OkHttpClient, json: Json): Retrofit =
        createRetrofit(AUTH_URL, client, json)

    @Provides
    @Singleton
    @BudgetRetrofit
    fun provideBudgetRetrofit(@AuthenticatedClient client: OkHttpClient, json: Json): Retrofit =
        createRetrofit(BUDGET_URL, client, json)

    @Provides
    @Singleton
    @DashboardRetrofit
    fun provideDashboardRetrofit(@AuthenticatedClient client: OkHttpClient, json: Json): Retrofit =
        createRetrofit(DASHBOARD_URL, client, json)

    @Provides
    @Singleton
    @TransactionRetrofit
    fun provideTransactionRetrofit(
        @AuthenticatedClient client: OkHttpClient,
        json: Json
    ): Retrofit =
        createRetrofit(TRANSACTION_URL, client, json)

    @Provides
    @Singleton
    @GoalRetrofit
    fun provideGoalRetrofit(@AuthenticatedClient client: OkHttpClient, json: Json): Retrofit =
        createRetrofit(GOAL_URL, client, json)

    // --- API Providers ---

    @Provides
    @Singleton
    fun provideAuthApi(@AuthRetrofit retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideBudgetApi(@BudgetRetrofit retrofit: Retrofit): BudgetApi =
        retrofit.create(BudgetApi::class.java)

    @Provides
    @Singleton
    fun provideCategoryApi(@BudgetRetrofit retrofit: Retrofit): CategoryApi =
        retrofit.create(CategoryApi::class.java)

    @Provides
    @Singleton
    fun provideTransactionApi(@TransactionRetrofit retrofit: Retrofit): TransactionApi =
        retrofit.create(TransactionApi::class.java)

    @Provides
    @Singleton
    fun provideGoalApi(@GoalRetrofit retrofit: Retrofit): GoalApi =
        retrofit.create(GoalApi::class.java)

    @Provides
    @Singleton
    fun provideDashboardApi(@DashboardRetrofit retrofit: Retrofit): DashboardApi =
        retrofit.create(DashboardApi::class.java)

    private fun createRetrofit(baseUrl: String, client: OkHttpClient, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}