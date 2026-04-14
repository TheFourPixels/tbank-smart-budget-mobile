package com.tbank.smartbudget.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tbank.smartbudget.core.network.remote.api.UnauthorizedApi
import com.tbank.smartbudget.core.network.remote.api.BudgetApi
import com.tbank.smartbudget.core.network.remote.api.CategoryApi
import com.tbank.smartbudget.core.network.remote.api.DashboardApi
import com.tbank.smartbudget.core.network.remote.api.GoalApi
import com.tbank.smartbudget.core.network.remote.api.TransactionApi
import com.tbank.smartbudget.core.network.remote.api.AuthorizedUserApi
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
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "192.168.0.188"
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

    // --- API Providers ---

    @Provides
    @Singleton
    fun provideAuthApi(@PublicClient client: OkHttpClient, json: Json): UnauthorizedApi =
        createRetrofit(AUTH_URL, client, json).create(UnauthorizedApi::class.java)

    @Provides
    @Singleton
    fun provideBudgetApi(@AuthenticatedClient client: OkHttpClient, json: Json): BudgetApi =
       createRetrofit(BUDGET_URL, client, json).create(BudgetApi::class.java)

    @Provides
    @Singleton
    fun provideCategoryApi(@AuthenticatedClient client: OkHttpClient, json: Json): CategoryApi =
        createRetrofit(BUDGET_URL, client, json).create(CategoryApi::class.java)

    @Provides
    @Singleton
    fun provideTransactionApi(@AuthenticatedClient client: OkHttpClient, json: Json): TransactionApi =
        createRetrofit(TRANSACTION_URL, client, json).create(TransactionApi::class.java)

    @Provides
    @Singleton
    fun provideGoalApi(@AuthenticatedClient client: OkHttpClient, json: Json): GoalApi =
        createRetrofit(GOAL_URL, client, json).create(GoalApi::class.java)


    @Provides
    @Singleton
    fun provideDashboardApi(@AuthenticatedClient client: OkHttpClient, json: Json): DashboardApi =
        createRetrofit(DASHBOARD_URL, client, json).create(DashboardApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(@AuthenticatedClient client: OkHttpClient, json: Json): AuthorizedUserApi =
        createRetrofit(AUTH_URL, client, json).create(AuthorizedUserApi::class.java)

    private fun createRetrofit(baseUrl: String, client: OkHttpClient, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}