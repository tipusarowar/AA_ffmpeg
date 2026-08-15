package com.dhaka.connect.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * A conversation is modeled from the point of view of [ownerId] talking to [otherUserId],
 * since this app only ever renders the signed-in user's own inbox.
 */
@Entity(
    tableName = "conversations",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["ownerId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["otherUserId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("ownerId"), Index("otherUserId"), Index(value = ["ownerId", "otherUserId"], unique = true)]
)
data class ConversationEntity(
    @PrimaryKey val id: String,
    val ownerId: String,
    val otherUserId: String,
    val lastMessage: String,
    val lastMessageAt: Long
)
