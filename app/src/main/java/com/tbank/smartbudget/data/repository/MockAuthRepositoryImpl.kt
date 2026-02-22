//package com.tbank.smartbudget.data.repository
//
//import com.tbank.smartbudget.domain.repository.AuthRepository
//import kotlinx.coroutines.delay
//import javax.inject.Inject
//
//class MockAuthRepositoryImpl @Inject constructor() : AuthRepository {
//
//    // Имитация базы данных пользователей
//    private val existingUsers = mapOf(
//        "demo@mail.ru" to "Валерия",
//        "test@tbank.ru" to "Александр",
//        "admin@admin.com" to "Администратор"
//    )
//
//    override suspend fun checkUserExistence(email: String): Result<String?> {
//        delay(1000) // Имитация запроса к серверу
//        val name = existingUsers[email.lowercase().trim()]
//        return Result.success(name)
//    }
//}