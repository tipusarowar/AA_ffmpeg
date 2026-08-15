package com.dhaka.connect.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhaka.connect.data.local.dao.PostFeedItem
import com.dhaka.connect.data.local.entities.UserEntity
import com.dhaka.connect.data.repository.PostRepository
import com.dhaka.connect.data.repository.SocialRepository
import com.dhaka.connect.data.repository.UserRepository
import com.dhaka.connect.data.session.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FeedUiState(
    val currentUser: UserEntity? = null,
    val posts: List<PostFeedItem> = emptyList(),
    val unreadNotifications: Int = 0,
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModel(
    private val postRepository: PostRepository,
    private val socialRepository: SocialRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val currentUserId = sessionManager.currentUserId.filterNotNull()

    val uiState: StateFlow<FeedUiState> = currentUserId.flatMapLatest { userId ->
        combine(
            userRepository.observeUser(userId),
            postRepository.feed(userId),
            socialRepository.unreadNotificationCount(userId)
        ) { user, posts, unread ->
            FeedUiState(currentUser = user, posts = posts, unreadNotifications = unread, isLoading = false)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeedUiState())

    fun toggleLike(post: PostFeedItem) {
        val me = uiState.value.currentUser ?: return
        viewModelScope.launch {
            postRepository.toggleLike(post.id, me.id)
            if (!post.likedByMe) {
                socialRepository.notifyLike(post.authorId, me, post.id)
            }
        }
    }
}
