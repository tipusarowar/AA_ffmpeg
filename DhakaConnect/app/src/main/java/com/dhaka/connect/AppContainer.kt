package com.dhaka.connect

import android.content.Context
import com.dhaka.connect.data.local.AppDatabase
import com.dhaka.connect.data.repository.GroupRepository
import com.dhaka.connect.data.repository.MessageRepository
import com.dhaka.connect.data.repository.PostRepository
import com.dhaka.connect.data.repository.SocialRepository
import com.dhaka.connect.data.repository.UserRepository
import com.dhaka.connect.data.session.SessionManager
import kotlinx.coroutines.CoroutineScope

/** Simple hand-rolled dependency container, wired once from [DhakaConnectApp]. */
class AppContainer(context: Context, applicationScope: CoroutineScope) {

    private val database = AppDatabase.getInstance(context, applicationScope)

    val sessionManager = SessionManager(context)

    val userRepository = UserRepository(database.userDao())
    val postRepository = PostRepository(database.postDao())
    val socialRepository = SocialRepository(database.friendshipDao(), database.notificationDao())
    val groupRepository = GroupRepository(database.groupDao())
    val messageRepository = MessageRepository(database.messageDao())
}
