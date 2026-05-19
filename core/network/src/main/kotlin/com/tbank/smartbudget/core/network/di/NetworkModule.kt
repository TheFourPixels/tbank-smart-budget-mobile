package com.tbank.smartbudget.core.network.di

import com.tbank.smartbudget.core.network.remote.api.AuthorizedUserApi
import com.tbank.smartbudget.core.network.remote.api.BankApi
import com.tbank.smartbudget.core.network.remote.api.BudgetApi
import com.tbank.smartbudget.core.network.remote.api.CategoryApi
import com.tbank.smartbudget.core.network.remote.api.DashboardApi
import com.tbank.smartbudget.core.network.remote.api.GoalApi
import com.tbank.smartbudget.core.network.remote.api.TransactionApi
import com.tbank.smartbudget.core.network.remote.api.UnauthorizedApi
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
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://192.168.0.106:8080"

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

    @Provides
    @Singleton
    @AuthenticatedClient
    fun provideRetrofit(@AuthenticatedClient client: OkHttpClient, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    @PublicClient
    fun providePublicRetrofit(@PublicClient client: OkHttpClient, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    // --- API Providers ---

    @Provides
    @Singleton
    fun provideAuthApi(@PublicClient retrofit: Retrofit): UnauthorizedApi =
        retrofit.create(UnauthorizedApi::class.java)

    @Provides
    @Singleton
    fun provideBudgetApi(@AuthenticatedClient retrofit: Retrofit): BudgetApi =
       retrofit.create(BudgetApi::class.java)

    @Provides
    @Singleton
    fun provideCategoryApi(@AuthenticatedClient retrofit: Retrofit): CategoryApi =
        retrofit.create(CategoryApi::class.java)

    @Provides
    @Singleton
    fun provideTransactionApi(@AuthenticatedClient retrofit: Retrofit): TransactionApi =
        retrofit.create(TransactionApi::class.java)

    @Provides
    @Singleton
    fun provideGoalApi(@AuthenticatedClient retrofit: Retrofit): GoalApi =
        retrofit.create(GoalApi::class.java)

    @Provides
    @Singleton
    fun provideDashboardApi(@AuthenticatedClient retrofit: Retrofit): DashboardApi =
        retrofit.create(DashboardApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(@AuthenticatedClient retrofit: Retrofit): AuthorizedUserApi =
        retrofit.create(AuthorizedUserApi::class.java)

    @Provides
    @Singleton
    fun provideBankApi(@AuthenticatedClient retrofit: Retrofit): BankApi =
        retrofit.create(BankApi::class.java)
}
