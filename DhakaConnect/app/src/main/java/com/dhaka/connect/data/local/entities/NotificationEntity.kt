package com.dhaka.connect.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class NotificationType { LIKE, COMMENT, FRIEND_REQUEST, FRIEND_ACCEPTED, GROUP_JOIN }

@Entity(
    tableName = "notifications",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["recipientId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("recipientId")]
)
data class NotificationEntity(
    @PrimaryKey val id: String,
    val recipientId: String,
    val actorName: String,
    val actorAvatar: String,
    val type: NotificationType,
    val message: String,
    val relatedId: String? = null,
    val isRead: Boolean = false,
    val createdAt: Long
)
