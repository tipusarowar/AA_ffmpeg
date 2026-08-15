package com.dhaka.connect.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhaka.connect.data.repository.PostRepository
import com.dhaka.connect.data.session.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CreatePostViewModel(
    private val postRepository: PostRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    fun submitPost(content: String, imageUrl: String?, area: String?, groupId: String?, onDone: () -> Unit) {
        if (content.isBlank() && imageUrl == null) return
        viewModelScope.launch {
            val userId = sessionManager.currentUserId.first() ?: return@launch
            postRepository.createPost(userId, content, imageUrl, area, groupId)
            onDone()
        }
    }
}
