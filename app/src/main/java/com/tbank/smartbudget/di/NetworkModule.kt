package com.tbank.smartbudget.di

import com.tbank.smartbudget.data.remote.api.BudgetApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BUDGET_BASE_URL = "http://158.160.208.149:8081/api/v1/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Логируем тело запросов
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideBudgetApi(okHttpClient: OkHttpClient): BudgetApi {
        return Retrofit.Builder()
            .baseUrl(BUDGET_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BudgetApi::class.java)
    }
}