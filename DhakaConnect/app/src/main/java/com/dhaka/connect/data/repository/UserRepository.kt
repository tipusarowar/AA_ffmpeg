package com.dhaka.connect.data.repository

import com.dhaka.connect.data.PasswordHasher
import com.dhaka.connect.data.local.dao.UserDao
import com.dhaka.connect.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

sealed class AuthResult {
    data class Success(val user: UserEntity) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class UserRepository(private val userDao: UserDao) {

    fun observeUser(userId: String): Flow<UserEntity?> = userDao.observeUser(userId)

    suspend fun getUser(userId: String): UserEntity? = userDao.getUser(userId)

    fun searchUsers(query: String, currentUserId: String): Flow<List<UserEntity>> =
        userDao.searchUsers(query, currentUserId)

    fun suggestedFriends(currentUserId: String): Flow<List<UserEntity>> =
        userDao.observeSuggestedUsers(currentUserId)

    suspend fun login(phone: String, password: String): AuthResult {
        val user = userDao.getUserByPhone(phone.trim())
            ?: return AuthResult.Error("No account found for this phone number.")
        return if (user.passwordHash == PasswordHasher.hash(password)) {
            AuthResult.Success(user)
        } else {
            AuthResult.Error("Incorrect password. Please try again.")
        }
    }

    suspend fun register(fullName: String, phone: String, password: String, area: String): AuthResult {
        val trimmedPhone = phone.trim()
        if (userDao.getUserByPhone(trimmedPhone) != null) {
            return AuthResult.Error("An account with this phone number already exists.")
        }
        val user = UserEntity(
            id = UUID.randomUUID().toString(),
            fullName = fullName.trim(),
            phone = trimmedPhone,
            passwordHash = PasswordHasher.hash(password),
            bio = "New to DhakaConnect 👋",
            area = area,
            avatarUrl = "https://i.pravatar.cc/150?u=$trimmedPhone",
            coverUrl = "https://picsum.photos/seed/$trimmedPhone/800/300",
            createdAt = System.currentTimeMillis()
        )
        userDao.insert(user)
        return AuthResult.Success(user)
    }

    suspend fun updateProfile(user: UserEntity) = userDao.update(user)
}
