package com.dhaka.connect.data.repository

import com.dhaka.connect.data.local.dao.FriendshipDao
import com.dhaka.connect.data.local.dao.NotificationDao
import com.dhaka.connect.data.local.entities.FriendshipEntity
import com.dhaka.connect.data.local.entities.FriendshipStatus
import com.dhaka.connect.data.local.entities.NotificationEntity
import com.dhaka.connect.data.local.entities.NotificationType
import com.dhaka.connect.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class SocialRepository(
    private val friendshipDao: FriendshipDao,
    private val notificationDao: NotificationDao
) {

    fun friends(userId: String): Flow<List<UserEntity>> = friendshipDao.observeFriends(userId)

    fun incomingRequests(userId: String): Flow<List<FriendshipEntity>> =
        friendshipDao.observeIncomingRequests(userId)

    fun pendingRequestCount(userId: String): Flow<Int> = friendshipDao.observePendingCount(userId)

    suspend fun sendFriendRequest(fromUserId: String, toUserId: String) {
        val existing = friendshipDao.getFriendship(fromUserId, toUserId)
        if (existing != null) return
        friendshipDao.insert(
            FriendshipEntity(
                id = UUID.randomUUID().toString(),
                requesterId = fromUserId,
                targetId = toUserId,
                status = FriendshipStatus.PENDING,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun acceptRequest(requestId: String, requesterId: String, currentUser: UserEntity) {
        friendshipDao.updateStatus(requestId, FriendshipStatus.ACCEPTED)
        notificationDao.insert(
            NotificationEntity(
                id = UUID.randomUUID().toString(),
                recipientId = requesterId,
                actorName = currentUser.fullName,
                actorAvatar = currentUser.avatarUrl,
                type = NotificationType.FRIEND_ACCEPTED,
                message = "accepted your friend request.",
                relatedId = currentUser.id,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun declineRequest(requestId: String) = friendshipDao.delete(requestId)

    suspend fun getUser(userId: String): UserEntity? = friendshipDao.getUser(userId)

    suspend fun friendshipBetween(userIdA: String, userIdB: String): FriendshipEntity? =
        friendshipDao.getFriendship(userIdA, userIdB)

    fun notifications(userId: String): Flow<List<NotificationEntity>> =
        notificationDao.observeNotifications(userId)

    fun unreadNotificationCount(userId: String): Flow<Int> = notificationDao.observeUnreadCount(userId)

    suspend fun markAllNotificationsRead(userId: String) = notificationDao.markAllRead(userId)

    suspend fun notifyLike(postAuthorId: String, actor: UserEntity, postId: String) {
        if (postAuthorId == actor.id) return
        notificationDao.insert(
            NotificationEntity(
                id = UUID.randomUUID().toString(),
                recipientId = postAuthorId,
                actorName = actor.fullName,
                actorAvatar = actor.avatarUrl,
                type = NotificationType.LIKE,
                message = "liked your post.",
                relatedId = postId,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun notifyComment(postAuthorId: String, actor: UserEntity, postId: String) {
        if (postAuthorId == actor.id) return
        notificationDao.insert(
            NotificationEntity(
                id = UUID.randomUUID().toString(),
                recipientId = postAuthorId,
                actorName = actor.fullName,
                actorAvatar = actor.avatarUrl,
                type = NotificationType.COMMENT,
                message = "commented on your post.",
                relatedId = postId,
                createdAt = System.currentTimeMillis()
            )
        )
    }
}
