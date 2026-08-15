package com.dhaka.connect.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class FriendshipStatus { PENDING, ACCEPTED }

/**
 * requesterId sent the friend request to targetId.
 * status PENDING = awaiting targetId's response; ACCEPTED = friends.
 */
@Entity(
    tableName = "friendships",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["requesterId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["targetId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("requesterId"), Index("targetId"), Index(value = ["requesterId", "targetId"], unique = true)]
)
data class FriendshipEntity(
    @PrimaryKey val id: String,
    val requesterId: String,
    val targetId: String,
    val status: FriendshipStatus,
    val createdAt: Long
)
