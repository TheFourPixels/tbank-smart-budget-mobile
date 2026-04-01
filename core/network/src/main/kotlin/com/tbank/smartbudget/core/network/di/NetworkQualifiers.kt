package com.tbank.smartbudget.core.network.di

import javax.inject.Qualifier

@Qualifier @Retention(AnnotationRetention.BINARY) annotation class PublicClient
@Qualifier @Retention(AnnotationRetention.BINARY) annotation class AuthenticatedClient

@Qualifier @Retention(AnnotationRetention.BINARY) annotation class AuthRetrofit
@Qualifier @Retention(AnnotationRetention.BINARY) annotation class BudgetRetrofit
@Qualifier @Retention(AnnotationRetention.BINARY) annotation class TransactionRetrofit
@Qualifier @Retention(AnnotationRetention.BINARY) annotation class GoalRetrofit
@Qualifier @Retention(AnnotationRetention.BINARY) annotation class DashboardRetrofit