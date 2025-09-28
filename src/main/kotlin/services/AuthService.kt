package services

import models.User

class AuthService(private val hashService: HashService) {
    private val users = mutableListOf<User>()

    init {
        // Предустановленные пользователи
        val salt1 = hashService.generateSalt()
        val salt2 = hashService.generateSalt()

        users.addAll(listOf(
            User("alice", hashService.hashPassword("qwerty", salt1), salt1),
            User("bob", hashService.hashPassword("password", salt2), salt2),
            User("admin", hashService.hashPassword("admin123", salt1), salt1)
        ))
    }

    fun authenticate(login: String, password: String): Int {
        val user = users.find { it.login == login }
        return when {
            user == null -> 3 // Неверный логин
            !hashService.verifyPassword(password, user.salt, user.passwordHash) -> 2 // Неверный пароль
            else -> 0 // Успешная аутентификация
        }
    }
}