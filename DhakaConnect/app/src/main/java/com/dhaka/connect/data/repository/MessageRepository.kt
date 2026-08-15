package com.dhaka.connect.data.repository

import com.dhaka.connect.data.local.dao.ConversationWithUser
import com.dhaka.connect.data.local.dao.MessageDao
import com.dhaka.connect.data.local.entities.ConversationEntity
import com.dhaka.connect.data.local.entities.MessageEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class MessageRepository(private val messageDao: MessageDao) {

    fun conversations(ownerId: String): Flow<List<ConversationWithUser>> =
        messageDao.observeConversations(ownerId)

    fun messages(conversationId: String): Flow<List<MessageEntity>> =
        messageDao.observeMessages(conversationId)

    suspend fun getOrCreateConversation(ownerId: String, otherUserId: String): String {
        val existing = messageDao.findConversation(ownerId, otherUserId)
        if (existing != null) return existing.id
        val conversation = ConversationEntity(
            id = UUID.randomUUID().toString(),
            ownerId = ownerId,
            otherUserId = otherUserId,
            lastMessage = "",
            lastMessageAt = System.currentTimeMillis()
        )
        messageDao.insertConversation(conversation)
        return conversation.id
    }

    suspend fun sendMessage(conversationId: String, senderId: String, content: String) {
        val now = System.currentTimeMillis()
        messageDao.insertMessage(
            MessageEntity(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                senderId = senderId,
                content = content,
                sentAt = now
            )
        )
        messageDao.touchConversation(conversationId, content, now)
    }
}
