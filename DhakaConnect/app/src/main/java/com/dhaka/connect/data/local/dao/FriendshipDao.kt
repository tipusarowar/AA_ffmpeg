package com.dhaka.connect.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dhaka.connect.data.local.entities.FriendshipEntity
import com.dhaka.connect.data.local.entities.FriendshipStatus
import com.dhaka.connect.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendshipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(friendship: FriendshipEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(friendships: List<FriendshipEntity>)

    @Query(
        """
        SELECT u.* FROM users u
        JOIN friendships f ON (
            (f.requesterId = :userId AND f.targetId = u.id) OR
            (f.targetId = :userId AND f.requesterId = u.id)
        )
        WHERE f.status = 'ACCEPTED'
        ORDER BY u.fullName ASC
        """
    )
    fun observeFriends(userId: String): Flow<List<UserEntity>>

    @Query(
        """
        SELECT f.* FROM friendships f
        WHERE f.targetId = :userId AND f.status = 'PENDING'
        ORDER BY f.createdAt DESC
        """
    )
    fun observeIncomingRequests(userId: String): Flow<List<FriendshipEntity>>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUser(userId: String): UserEntity?

    @Query(
        """
        SELECT * FROM friendships
        WHERE (requesterId = :userIdA AND targetId = :userIdB)
           OR (requesterId = :userIdB AND targetId = :userIdA)
        LIMIT 1
        """
    )
    suspend fun getFriendship(userIdA: String, userIdB: String): FriendshipEntity?

    @Query("UPDATE friendships SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: FriendshipStatus)

    @Query("DELETE FROM friendships WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT COUNT(*) FROM friendships WHERE targetId = :userId AND status = 'PENDING'")
    fun observePendingCount(userId: String): Flow<Int>
}
