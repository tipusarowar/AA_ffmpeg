package com.dhaka.connect.ui.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhaka.connect.data.local.dao.GroupWithMembership
import com.dhaka.connect.data.local.dao.PostFeedItem
import com.dhaka.connect.data.repository.GroupRepository
import com.dhaka.connect.data.repository.PostRepository
import com.dhaka.connect.data.session.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GroupDetailUiState(
    val group: GroupWithMembership? = null,
    val posts: List<PostFeedItem> = emptyList(),
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class GroupDetailViewModel(
    private val groupId: String,
    private val groupRepository: GroupRepository,
    private val postRepository: PostRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val currentUserId = sessionManager.currentUserId.filterNotNull()

    val uiState: StateFlow<GroupDetailUiState> = currentUserId.flatMapLatest { userId ->
        combine(
            groupRepository.group(groupId, userId),
            postRepository.groupFeed(groupId, userId)
        ) { group, posts ->
            GroupDetailUiState(group = group, posts = posts, isLoading = false)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GroupDetailUiState())

    fun toggleMembership() {
        val group = uiState.value.group ?: return
        viewModelScope.launch {
            val userId = currentUserId.first()
            if (group.isMember) groupRepository.leaveGroup(group.id, userId) else groupRepository.joinGroup(group.id, userId)
        }
    }

    fun toggleLike(post: PostFeedItem) {
        viewModelScope.launch {
            val userId = currentUserId.first()
            postRepository.toggleLike(post.id, userId)
        }
    }
}
