package com.dhaka.connect.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "posts",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["authorId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = GroupEntity::class, parentColumns = ["id"], childColumns = ["groupId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("authorId"), Index("groupId")]
)
data class PostEntity(
    @PrimaryKey val id: String,
    val authorId: String,
    val content: String,
    val imageUrl: String? = null,
    val area: String? = null,
    val groupId: String? = null,
    val baseLikeCount: Int = 0,
    val createdAt: Long
)
