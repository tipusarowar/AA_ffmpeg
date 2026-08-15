package com.dhaka.connect.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dhaka.connect.data.SeedData
import com.dhaka.connect.data.local.dao.FriendshipDao
import com.dhaka.connect.data.local.dao.GroupDao
import com.dhaka.connect.data.local.dao.MessageDao
import com.dhaka.connect.data.local.dao.NotificationDao
import com.dhaka.connect.data.local.dao.PostDao
import com.dhaka.connect.data.local.dao.UserDao
import com.dhaka.connect.data.local.entities.CommentEntity
import com.dhaka.connect.data.local.entities.ConversationEntity
import com.dhaka.connect.data.local.entities.FriendshipEntity
import com.dhaka.connect.data.local.entities.GroupEntity
import com.dhaka.connect.data.local.entities.GroupMemberEntity
import com.dhaka.connect.data.local.entities.MessageEntity
import com.dhaka.connect.data.local.entities.NotificationEntity
import com.dhaka.connect.data.local.entities.PostEntity
import com.dhaka.connect.data.local.entities.PostLikeEntity
import com.dhaka.connect.data.local.entities.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        PostEntity::class,
        PostLikeEntity::class,
        CommentEntity::class,
        FriendshipEntity::class,
        GroupEntity::class,
        GroupMemberEntity::class,
        ConversationEntity::class,
        MessageEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun friendshipDao(): FriendshipDao
    abstract fun groupDao(): GroupDao
    abstract fun messageDao(): MessageDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, applicationScope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dhaka_connect.db"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                        super.onCreate(db)
                        applicationScope.launch {
                            INSTANCE?.let { SeedData.populate(it) }
                        }
                    }
                }).build().also { INSTANCE = it }
            }
        }
    }
}
