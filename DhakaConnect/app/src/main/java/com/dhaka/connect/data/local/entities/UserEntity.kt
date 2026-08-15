package com.dhaka.connect.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val fullName: String,
    val phone: String,
    val passwordHash: String,
    val bio: String = "",
    val area: String,
    val avatarUrl: String,
    val coverUrl: String,
    val createdAt: Long
)
