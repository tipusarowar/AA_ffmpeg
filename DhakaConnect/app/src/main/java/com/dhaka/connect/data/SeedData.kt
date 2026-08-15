package com.dhaka.connect.data

import com.dhaka.connect.data.local.AppDatabase
import com.dhaka.connect.data.local.entities.CommentEntity
import com.dhaka.connect.data.local.entities.ConversationEntity
import com.dhaka.connect.data.local.entities.FriendshipEntity
import com.dhaka.connect.data.local.entities.FriendshipStatus
import com.dhaka.connect.data.local.entities.GroupEntity
import com.dhaka.connect.data.local.entities.GroupMemberEntity
import com.dhaka.connect.data.local.entities.MessageEntity
import com.dhaka.connect.data.local.entities.NotificationEntity
import com.dhaka.connect.data.local.entities.NotificationType
import com.dhaka.connect.data.local.entities.PostEntity
import com.dhaka.connect.data.local.entities.PostLikeEntity
import com.dhaka.connect.data.local.entities.UserEntity
import java.util.UUID
import java.util.concurrent.TimeUnit

/** Demo account so a fresh install always has a populated feed: phone 01711000000 / password dhaka123. */
object DemoAccount {
    const val ID = "u_me"
    const val PHONE = "01711000000"
    const val PASSWORD = "dhaka123"
}

/** One-time population of the local database so the app feels alive without a real backend. */
object SeedData {

    suspend fun populate(db: AppDatabase) {
        val now = System.currentTimeMillis()
        fun hoursAgo(h: Long) = now - TimeUnit.HOURS.toMillis(h)
        fun daysAgo(d: Long) = now - TimeUnit.DAYS.toMillis(d)

        val me = UserEntity(
            id = DemoAccount.ID,
            fullName = "Rahim Uddin",
            phone = DemoAccount.PHONE,
            passwordHash = PasswordHasher.hash(DemoAccount.PASSWORD),
            bio = "Loves the smell of rain on Dhanmondi Lake. Rickshaw enthusiast.",
            area = "Dhanmondi",
            avatarUrl = "https://i.pravatar.cc/150?img=12",
            coverUrl = "https://picsum.photos/seed/dhanmondi-lake/800/300",
            createdAt = daysAgo(400)
        )

        val users = listOf(
            UserEntity("u_2", "Nusrat Jahan", "01711000001", PasswordHasher.hash("password"), "Foodie exploring Old Dhaka's biryani spots.", "Old Dhaka", "https://i.pravatar.cc/150?img=5", "https://picsum.photos/seed/old-dhaka/800/300", daysAgo(390)),
            UserEntity("u_3", "Tanvir Ahmed", "01711000002", PasswordHasher.hash("password"), "Software engineer @ Gulshan. Cricket every Friday.", "Gulshan", "https://i.pravatar.cc/150?img=15", "https://picsum.photos/seed/gulshan/800/300", daysAgo(380)),
            UserEntity("u_4", "Farzana Akter", "01711000003", PasswordHasher.hash("password"), "Dhaka University, Journalism '21.", "Dhanmondi", "https://i.pravatar.cc/150?img=32", "https://picsum.photos/seed/du-campus/800/300", daysAgo(370)),
            UserEntity("u_5", "Shakil Rana", "01711000004", PasswordHasher.hash("password"), "Traffic reporter. Always stuck at Mohakhali flyover.", "Mohammadpur", "https://i.pravatar.cc/150?img=51", "https://picsum.photos/seed/mohakhali/800/300", daysAgo(360)),
            UserEntity("u_6", "Ayesha Siddika", "01711000005", PasswordHasher.hash("password"), "Uttara Sector 11. Runs a small boutique.", "Uttara", "https://i.pravatar.cc/150?img=44", "https://picsum.photos/seed/uttara/800/300", daysAgo(350)),
            UserEntity("u_7", "Kamal Hossain", "01711000006", PasswordHasher.hash("password"), "Rickshaw art collector, Mirpur.", "Mirpur", "https://i.pravatar.cc/150?img=60", "https://picsum.photos/seed/mirpur/800/300", daysAgo(340)),
            UserEntity("u_8", "Sultana Parvin", "01711000007", PasswordHasher.hash("password"), "Bashundhara City regular. Movie nights.", "Bashundhara", "https://i.pravatar.cc/150?img=25", "https://picsum.photos/seed/bashundhara/800/300", daysAgo(330)),
            UserEntity("u_9", "Imran Khalid", "01711000008", PasswordHasher.hash("password"), "Banani foodie, plays football at the DOHS ground.", "Banani", "https://i.pravatar.cc/150?img=68", "https://picsum.photos/seed/banani/800/300", daysAgo(320)),
            UserEntity("u_10", "Mitu Chowdhury", "01711000009", PasswordHasher.hash("password"), "Rampura resident, loves Hatirjheel evenings.", "Rampura", "https://i.pravatar.cc/150?img=47", "https://picsum.photos/seed/hatirjheel/800/300", daysAgo(310))
        )

        val groups = listOf(
            GroupEntity("g_1", "Dhaka Traffic Alerts", "Live updates on jams, diversions and road works across Dhaka.", "Dhaka-wide", "https://picsum.photos/seed/dhaka-traffic/800/300", daysAgo(500)),
            GroupEntity("g_2", "Dhaka Food Lovers", "From Old Dhaka biryani to Gulshan cafes — share your finds.", "Dhaka-wide", "https://picsum.photos/seed/dhaka-food/800/300", daysAgo(480)),
            GroupEntity("g_3", "Dhaka University Alumni", "For graduates and current students of DU to reconnect.", "Shahbagh", "https://picsum.photos/seed/du-alumni/800/300", daysAgo(460)),
            GroupEntity("g_4", "Old Dhaka Heritage Walks", "Weekend walking tours of Lalbagh Fort, Ahsan Manzil and the old city.", "Old Dhaka", "https://picsum.photos/seed/lalbagh-fort/800/300", daysAgo(440)),
            GroupEntity("g_5", "Dhaka Tech Meetup", "Monthly meetups for developers and startups in Dhaka.", "Gulshan", "https://picsum.photos/seed/dhaka-tech/800/300", daysAgo(420))
        )

        val posts = listOf(
            PostEntity(newId(), "u_2", "Found the best kacchi biryani near Chawk Bazar today! Anyone want the exact address? 😋", "https://picsum.photos/seed/kacchi-biryani/800/500", "Old Dhaka", null, 24, hoursAgo(2)),
            PostEntity(newId(), "u_3", "Gulshan Lake looking beautiful this evening. Perfect weather for a walk.", "https://picsum.photos/seed/gulshan-lake-evening/800/500", "Gulshan", null, 41, hoursAgo(4)),
            PostEntity(newId(), "u_5", "Mohakhali flyover jammed again from 5-7pm. Take the Gulshan-2 route if you can, it's clearer today.", null, "Mohammadpur", "g_1", 18, hoursAgo(1)),
            PostEntity(newId(), "u_4", "Missing the DU campus during the Pohela Boishakh Mangal Shobhajatra. Best day of the year!", "https://picsum.photos/seed/mangal-shobhajatra/800/500", "Dhanmondi", "g_3", 63, daysAgo(2)),
            PostEntity(newId(), "u_7", "New rickshaw art piece I picked up from a painter in Mirpur 10. These artists deserve way more recognition.", "https://picsum.photos/seed/rickshaw-art/800/500", "Mirpur", null, 37, daysAgo(1)),
            PostEntity(newId(), "u_6", "Uttara Sector 11 park just got new lighting installed. Evening walks are so much nicer now.", null, "Uttara", null, 15, hoursAgo(6)),
            PostEntity(newId(), "u_9", "Football match at Banani DOHS ground this Friday evening, 6pm. Come join, need two more players!", null, "Banani", null, 9, hoursAgo(10)),
            PostEntity(newId(), "u_8", "Bashundhara City food court has a new bubble tea place. 10/10 would recommend the taro flavor.", "https://picsum.photos/seed/bubble-tea/800/500", "Bashundhara", "g_2", 28, daysAgo(1)),
            PostEntity(newId(), "u_10", "Hatirjheel at sunset never gets old. Took the water taxi today instead of walking.", "https://picsum.photos/seed/hatirjheel-sunset/800/500", "Rampura", null, 52, daysAgo(3)),
            PostEntity(newId(), "u_2", "Reminder: Lalbagh Fort heritage walk this Saturday morning, 8am sharp at the main gate. Free entry for group members!", "https://picsum.photos/seed/lalbagh-walk/800/500", "Old Dhaka", "g_4", 33, daysAgo(2)),
            PostEntity(newId(), DemoAccount.ID, "Just moved my desk near the window at Dhanmondi Lake view. Best decision this year.", "https://picsum.photos/seed/dhanmondi-view/800/500", "Dhanmondi", null, 12, hoursAgo(20)),
            PostEntity(newId(), "u_3", "Dhaka Tech Meetup was great last night — over 80 developers showed up! Slides from the Kotlin talk are in the group.", null, "Gulshan", "g_5", 46, daysAgo(4))
        )

        val comments = listOf(
            CommentEntity(newId(), posts[0].id, "u_9", "Please share! Been craving kacchi all week.", hoursAgo(1)),
            CommentEntity(newId(), posts[0].id, DemoAccount.ID, "Second this, Chawk Bazar biryani is unmatched.", hoursAgo(1)),
            CommentEntity(newId(), posts[1].id, "u_6", "Gulshan Lake walkway is underrated honestly.", hoursAgo(3)),
            CommentEntity(newId(), posts[2].id, DemoAccount.ID, "Thanks for the heads up, avoiding Mohakhali today.", hoursAgo(1)),
            CommentEntity(newId(), posts[4].id, "u_4", "The rickshaw art in Dhaka is basically folk art at this point, love it.", daysAgo(1)),
            CommentEntity(newId(), posts[9].id, DemoAccount.ID, "Count me in for Saturday!", daysAgo(1))
        )

        val postLikes = listOf(
            PostLikeEntity(posts[0].id, DemoAccount.ID),
            PostLikeEntity(posts[4].id, DemoAccount.ID),
            PostLikeEntity(posts[8].id, DemoAccount.ID)
        )

        val friendships = listOf(
            FriendshipEntity(newId(), DemoAccount.ID, "u_3", FriendshipStatus.ACCEPTED, daysAgo(200)),
            FriendshipEntity(newId(), "u_4", DemoAccount.ID, FriendshipStatus.ACCEPTED, daysAgo(150)),
            FriendshipEntity(newId(), DemoAccount.ID, "u_9", FriendshipStatus.ACCEPTED, daysAgo(90)),
            FriendshipEntity(newId(), "u_2", DemoAccount.ID, FriendshipStatus.ACCEPTED, daysAgo(60)),
            FriendshipEntity(newId(), "u_7", DemoAccount.ID, FriendshipStatus.PENDING, hoursAgo(30)),
            FriendshipEntity(newId(), "u_8", DemoAccount.ID, FriendshipStatus.PENDING, hoursAgo(5))
        )

        val groupMembers = listOf(
            GroupMemberEntity("g_1", DemoAccount.ID, daysAgo(80)),
            GroupMemberEntity("g_2", DemoAccount.ID, daysAgo(60)),
            GroupMemberEntity("g_1", "u_5", daysAgo(200)),
            GroupMemberEntity("g_2", "u_2", daysAgo(200)),
            GroupMemberEntity("g_2", "u_8", daysAgo(150)),
            GroupMemberEntity("g_3", "u_4", daysAgo(200)),
            GroupMemberEntity("g_3", "u_3", daysAgo(180)),
            GroupMemberEntity("g_4", "u_2", daysAgo(150)),
            GroupMemberEntity("g_5", "u_3", daysAgo(300))
        )

        val conv1 = ConversationEntity(newId(), DemoAccount.ID, "u_3", "See you at the meetup!", hoursAgo(5))
        val conv2 = ConversationEntity(newId(), DemoAccount.ID, "u_2", "Sure, I'll send the address now.", hoursAgo(1))
        val conversations = listOf(conv1, conv2)

        val messages = listOf(
            MessageEntity(newId(), conv1.id, "u_3", "Hey, are you coming to the tech meetup this month?", hoursAgo(8)),
            MessageEntity(newId(), conv1.id, DemoAccount.ID, "Yes! Wouldn't miss it. What time again?", hoursAgo(7)),
            MessageEntity(newId(), conv1.id, "u_3", "7pm at the Gulshan office. See you at the meetup!", hoursAgo(5)),
            MessageEntity(newId(), conv2.id, DemoAccount.ID, "That biryani place you posted looks amazing, what's the address?", hoursAgo(2)),
            MessageEntity(newId(), conv2.id, "u_2", "Sure, I'll send the address now.", hoursAgo(1))
        )

        val notifications = listOf(
            NotificationEntity(newId(), DemoAccount.ID, "Nusrat Jahan", users[0].avatarUrl, NotificationType.LIKE, "liked your post about Dhanmondi Lake view.", posts[10].id, false, hoursAgo(3)),
            NotificationEntity(newId(), DemoAccount.ID, "Farzana Akter", users[2].avatarUrl, NotificationType.COMMENT, "commented on your post.", posts[10].id, false, hoursAgo(5)),
            NotificationEntity(newId(), DemoAccount.ID, "Kamal Hossain", users[5].avatarUrl, NotificationType.FRIEND_REQUEST, "sent you a friend request.", "u_7", false, hoursAgo(30)),
            NotificationEntity(newId(), DemoAccount.ID, "Tanvir Ahmed", users[1].avatarUrl, NotificationType.FRIEND_ACCEPTED, "accepted your friend request.", "u_3", true, daysAgo(150)),
            NotificationEntity(newId(), DemoAccount.ID, "Dhaka Tech Meetup", groups[4].coverUrl, NotificationType.GROUP_JOIN, "posted an update in a group you follow.", "g_5", true, daysAgo(4))
        )

        db.userDao().insertAll(listOf(me) + users)
        db.groupDao().insertGroups(groups)
        db.postDao().insertPosts(posts)
        db.postDao().insertComments(comments)
        postLikes.forEach { db.postDao().like(it) }
        db.friendshipDao().insertAll(friendships)
        groupMembers.forEach { db.groupDao().insertMember(it) }
        conversations.forEach { db.messageDao().insertConversation(it) }
        db.messageDao().insertMessages(messages)
        db.notificationDao().insertAll(notifications)
    }

    private fun newId(): String = UUID.randomUUID().toString()
}
