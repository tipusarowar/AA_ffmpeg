package com.dhaka.connect.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhaka.connect.data.local.dao.PostFeedItem
import com.dhaka.connect.data.local.entities.FriendshipStatus
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FriendRelation { SELF, FRIENDS, REQUEST_SENT, REQUEST_RECEIVED, NONE }

data class ProfileUiState(
    val viewedUser: UserEntity? = null,
    val isOwnProfile: Boolean = false,
    val relation: FriendRelation = FriendRelation.NONE,
    val posts: List<PostFeedItem> = emptyList(),
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
    private val profileUserId: String,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val socialRepository: SocialRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val currentUserId = sessionManager.currentUserId.filterNotNull()

    val uiState: StateFlow<ProfileUiState> = currentUserId.flatMapLatest { myId ->
        combine(
            userRepository.observeUser(profileUserId),
            postRepository.userPosts(profileUserId, myId),
            socialRepository.friends(myId)
        ) { user, posts, myFriends ->
            val isOwn = myId == profileUserId
            val relation = when {
                isOwn -> FriendRelation.SELF
                myFriends.any { it.id == profileUserId } -> FriendRelation.FRIENDS
                else -> {
                    val friendship = socialRepository.friendshipBetween(myId, profileUserId)
                    when {
                        friendship == null -> FriendRelation.NONE
                        friendship.status == FriendshipStatus.PENDING && friendship.requesterId == myId -> FriendRelation.REQUEST_SENT
                        friendship.status == FriendshipStatus.PENDING -> FriendRelation.REQUEST_RECEIVED
                        else -> FriendRelation.NONE
                    }
                }
            }
            ProfileUiState(viewedUser = user, isOwnProfile = isOwn, relation = relation, posts = posts, isLoading = false)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileUiState())

    fun sendFriendRequest() {
        viewModelScope.launch {
            val myId = currentUserId.first()
            socialRepository.sendFriendRequest(myId, profileUserId)
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearSession()
            onLoggedOut()
        }
    }
}
