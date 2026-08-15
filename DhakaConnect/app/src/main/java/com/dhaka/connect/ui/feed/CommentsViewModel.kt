package com.dhaka.connect.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhaka.connect.data.local.dao.CommentWithAuthor
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

data class CommentsUiState(
    val currentUser: UserEntity? = null,
    val post: PostFeedItem? = null,
    val comments: List<CommentWithAuthor> = emptyList(),
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class CommentsViewModel(
    private val postId: String,
    private val postRepository: PostRepository,
    private val socialRepository: SocialRepository,
    private val userRepository: UserRepository,
    sessionManager: SessionManager
) : ViewModel() {

    private val currentUserId = sessionManager.currentUserId.filterNotNull()

    val uiState: StateFlow<CommentsUiState> = currentUserId.flatMapLatest { userId ->
        combine(
            userRepository.observeUser(userId),
            postRepository.post(postId, userId),
            postRepository.comments(postId)
        ) { user, post, comments ->
            CommentsUiState(currentUser = user, post = post, comments = comments, isLoading = false)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CommentsUiState())

    fun toggleLike() {
        val post = uiState.value.post ?: return
        val me = uiState.value.currentUser ?: return
        viewModelScope.launch {
            postRepository.toggleLike(post.id, me.id)
            if (!post.likedByMe) {
                socialRepository.notifyLike(post.authorId, me, post.id)
            }
        }
    }

    fun addComment(text: String) {
        if (text.isBlank()) return
        val post = uiState.value.post ?: return
        val me = uiState.value.currentUser ?: return
        viewModelScope.launch {
            postRepository.addComment(postId, me.id, text.trim())
            socialRepository.notifyComment(post.authorId, me, post.id)
        }
    }
}
