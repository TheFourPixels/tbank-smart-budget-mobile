package com.tbank.smartbudget.di

import com.tbank.smartbudget.data.remote.api.BudgetApi
import com.tbank.smartbudget.data.remote.api.CategoryApi // Добавили
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Для эмулятора Android используем 10.0.2.2
    // Если тестируете на реальном устройстве, поменяйте на IP вашего компьютера
    private const val BASE_URL = "http://10.0.2.2:8080/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideBudgetApi(retrofit: Retrofit): BudgetApi {
        return retrofit.create(BudgetApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryApi(retrofit: Retrofit): CategoryApi { // Добавили
        return retrofit.create(CategoryApi::class.java)
    }
}