package com.dhaka.connect.data.local

import androidx.room.TypeConverter
import com.dhaka.connect.data.local.entities.FriendshipStatus
import com.dhaka.connect.data.local.entities.NotificationType

class Converters {

    @TypeConverter
    fun fromFriendshipStatus(status: FriendshipStatus): String = status.name

    @TypeConverter
    fun toFriendshipStatus(value: String): FriendshipStatus = FriendshipStatus.valueOf(value)

    @TypeConverter
    fun fromNotificationType(type: NotificationType): String = type.name

    @TypeConverter
    fun toNotificationType(value: String): NotificationType = NotificationType.valueOf(value)
}
