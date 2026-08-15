package com.dhaka.connect.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dhaka.connect.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Update
    suspend fun update(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun observeUser(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUser(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE phone = :phone LIMIT 1")
    suspend fun getUserByPhone(phone: String): UserEntity?

    @Query(
        """
        SELECT * FROM users
        WHERE id != :excludeUserId
        AND (fullName LIKE '%' || :query || '%' OR area LIKE '%' || :query || '%')
        ORDER BY fullName ASC
        """
    )
    fun searchUsers(query: String, excludeUserId: String): Flow<List<UserEntity>>

    @Query(
        """
        SELECT * FROM users
        WHERE id != :currentUserId
        AND id NOT IN (
            SELECT requesterId FROM friendships WHERE targetId = :currentUserId
            UNION
            SELECT targetId FROM friendships WHERE requesterId = :currentUserId
        )
        ORDER BY RANDOM()
        """
    )
    fun observeSuggestedUsers(currentUserId: String): Flow<List<UserEntity>>

    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int
}
