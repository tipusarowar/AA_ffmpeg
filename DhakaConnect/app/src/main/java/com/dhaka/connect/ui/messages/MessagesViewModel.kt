package com.dhaka.connect.ui.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhaka.connect.data.local.dao.ConversationWithUser
import com.dhaka.connect.data.repository.MessageRepository
import com.dhaka.connect.data.session.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class MessagesViewModel(
    messageRepository: MessageRepository,
    sessionManager: SessionManager
) : ViewModel() {

    val conversations: StateFlow<List<ConversationWithUser>> = sessionManager.currentUserId
        .filterNotNull()
        .flatMapLatest { userId -> messageRepository.conversations(userId) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
