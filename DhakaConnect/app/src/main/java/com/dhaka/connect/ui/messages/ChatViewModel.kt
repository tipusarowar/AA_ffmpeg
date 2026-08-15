package com.dhaka.connect.ui.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhaka.connect.data.local.entities.MessageEntity
import com.dhaka.connect.data.local.entities.UserEntity
import com.dhaka.connect.data.repository.MessageRepository
import com.dhaka.connect.data.repository.UserRepository
import com.dhaka.connect.data.session.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatUiState(
    val otherUser: UserEntity? = null,
    val messages: List<MessageEntity> = emptyList(),
    val currentUserId: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModel(
    private val otherUserId: String,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val currentUserId = sessionManager.currentUserId.filterNotNull()
    private val conversationId = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            val myId = currentUserId.first()
            conversationId.value = messageRepository.getOrCreateConversation(myId, otherUserId)
        }
    }

    val uiState: StateFlow<ChatUiState> = kotlinx.coroutines.flow.combine(
        currentUserId,
        userRepository.observeUser(otherUserId),
        conversationId.flatMapLatest { id -> if (id == null) flowOf(emptyList()) else messageRepository.messages(id) }
    ) { myId, otherUser, messages ->
        ChatUiState(otherUser = otherUser, messages = messages, currentUserId = myId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChatUiState())

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val myId = currentUserId.first()
            val convId = conversationId.value ?: messageRepository.getOrCreateConversation(myId, otherUserId).also { conversationId.value = it }
            messageRepository.sendMessage(convId, myId, text.trim())
        }
    }
}
