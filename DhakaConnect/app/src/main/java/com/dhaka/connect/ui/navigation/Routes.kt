package com.dhaka.connect.ui.navigation

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val CREATE_POST = "create_post?groupId={groupId}"
    fun createPost(groupId: String? = null) = if (groupId == null) "create_post" else "create_post?groupId=$groupId"
    const val EDIT_PROFILE = "edit_profile"
    const val NOTIFICATIONS = "notifications"
    const val SEARCH = "search"
    const val MESSAGES = "messages"

    const val POST_COMMENTS = "post_comments/{postId}"
    fun postComments(postId: String) = "post_comments/$postId"

    const val PROFILE = "profile/{userId}"
    fun profile(userId: String) = "profile/$userId"

    const val GROUP_DETAIL = "group_detail/{groupId}"
    fun groupDetail(groupId: String) = "group_detail/$groupId"

    const val CHAT = "chat/{userId}"
    fun chat(userId: String) = "chat/$userId"
}

object HomeTabRoutes {
    const val FEED = "tab_feed"
    const val FRIENDS = "tab_friends"
    const val GROUPS = "tab_groups"
    const val MENU = "tab_menu"
}
