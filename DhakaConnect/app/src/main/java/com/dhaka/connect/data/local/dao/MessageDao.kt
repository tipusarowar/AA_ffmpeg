package com.dhaka.connect.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dhaka.connect.data.local.entities.ConversationEntity
import com.dhaka.connect.data.local.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

data class ConversationWithUser(
    val id: String,
    val ownerId: String,
    val otherUserId: String,
    val lastMessage: String,
    val lastMessageAt: Long,
    val otherUserName: String,
    val otherUserAvatar: String
)

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("SELECT * FROM conversations WHERE ownerId = :ownerId AND otherUserId = :otherUserId LIMIT 1")
    suspend fun findConversation(ownerId: String, otherUserId: String): ConversationEntity?

    @Query(
        """
        SELECT c.id as id, c.ownerId as ownerId, c.otherUserId as otherUserId,
               c.lastMessage as lastMessage, c.lastMessageAt as lastMessageAt,
               u.fullName as otherUserName, u.avatarUrl as otherUserAvatar
        FROM conversations c
        JOIN users u ON u.id = c.otherUserId
        WHERE c.ownerId = :ownerId
        ORDER BY c.lastMessageAt DESC
        """
    )
    fun observeConversations(ownerId: String): Flow<List<ConversationWithUser>>

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY sentAt ASC")
    fun observeMessages(conversationId: String): Flow<List<MessageEntity>>

    @Query("UPDATE conversations SET lastMessage = :lastMessage, lastMessageAt = :lastMessageAt WHERE id = :id")
    suspend fun touchConversation(id: String, lastMessage: String, lastMessageAt: Long)
}
