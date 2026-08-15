package com.dhaka.connect.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhaka.connect.data.local.entities.FriendshipEntity
import com.dhaka.connect.data.local.entities.UserEntity
import com.dhaka.connect.data.repository.SocialRepository
import com.dhaka.connect.data.repository.UserRepository
import com.dhaka.connect.data.session.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class IncomingRequest(val friendship: FriendshipEntity, val requester: UserEntity)

data class FriendsUiState(
    val currentUser: UserEntity? = null,
    val friends: List<UserEntity> = emptyList(),
    val incomingRequests: List<IncomingRequest> = emptyList(),
    val suggestions: List<UserEntity> = emptyList(),
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class FriendsViewModel(
    private val socialRepository: SocialRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val currentUserId = sessionManager.currentUserId.filterNotNull()

    val uiState: StateFlow<FriendsUiState> = currentUserId.flatMapLatest { userId ->
        combine(
            userRepository.observeUser(userId),
            socialRepository.friends(userId),
            socialRepository.incomingRequests(userId),
            userRepository.suggestedFriends(userId)
        ) { user, friends, requests, suggestions ->
            val resolvedRequests = requests.mapNotNull { req ->
                socialRepository.getUser(req.requesterId)?.let { requester -> IncomingRequest(req, requester) }
            }
            FriendsUiState(user, friends, resolvedRequests, suggestions, isLoading = false)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FriendsUiState())

    fun sendFriendRequest(targetId: String) {
        viewModelScope.launch {
            val me = currentUserId.first()
            socialRepository.sendFriendRequest(me, targetId)
        }
    }

    fun acceptRequest(request: IncomingRequest) {
        val me = uiState.value.currentUser ?: return
        viewModelScope.launch {
            socialRepository.acceptRequest(request.friendship.id, request.requester.id, me)
        }
    }

    fun declineRequest(request: IncomingRequest) {
        viewModelScope.launch {
            socialRepository.declineRequest(request.friendship.id)
        }
    }
}
