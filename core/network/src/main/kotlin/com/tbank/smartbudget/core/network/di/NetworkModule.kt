package com.tbank.smartbudget.core.network.di

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
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // --- Base URLs ---
    private const val AUTH_URL = "http://192.168.0.188:8089/"
    private const val BUDGET_URL = "http://192.168.0.188:8081/"
    private const val TRANSACTION_URL = "http://192.168.0.188:8083/"
    private const val GOAL_URL = "http://192.168.0.188:8087/"
    private const val DASHBOARD_URL = "http://192.168.0.188:8088/"

    // --- Qualifiers ---
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthService

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BudgetService

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TransactionService

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class GoalService

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DashboardService

    // --- Shared OkHttpClient with AuthInterceptor ---
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        errorInterceptor: ErrorInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(errorInterceptor)
            .build()
    }

    // --- Retrofit Instances ---

    @Provides
    @Singleton
    @AuthService
    fun provideAuthRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AUTH_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @BudgetService
    fun provideBudgetRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BUDGET_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @TransactionService
    fun provideTransactionRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TRANSACTION_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @GoalService
    fun provideGoalRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GOAL_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @DashboardService
    fun provideDashboardRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DASHBOARD_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // --- API Interfaces ---

    @Provides
    @Singleton
    fun provideAuthApi(@AuthService retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBudgetApi(@BudgetService retrofit: Retrofit): BudgetApi {
        return retrofit.create(BudgetApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryApi(@BudgetService retrofit: Retrofit): CategoryApi {
        return retrofit.create(CategoryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTransactionApi(@TransactionService retrofit: Retrofit): TransactionApi {
        return retrofit.create(TransactionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGoalApi(@GoalService retrofit: Retrofit): GoalApi {
        return retrofit.create(GoalApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDashboardApi(@DashboardService retrofit: Retrofit): DashboardApi {
        return retrofit.create(DashboardApi::class.java)
    }
}