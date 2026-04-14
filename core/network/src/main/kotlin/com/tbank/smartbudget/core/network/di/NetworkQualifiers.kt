package com.tbank.smartbudget.core.network.di

import javax.inject.Qualifier

@Qualifier @Retention(AnnotationRetention.BINARY) annotation class PublicClient
@Qualifier @Retention(AnnotationRetention.BINARY) annotation class AuthenticatedClient