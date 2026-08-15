package com.dhaka.connect.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhaka.connect.data.local.entities.UserEntity
import com.dhaka.connect.data.repository.UserRepository
import com.dhaka.connect.data.session.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeMenuViewModel(
    userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    val currentUser: StateFlow<UserEntity?> = sessionManager.currentUserId.filterNotNull()
        .flatMapLatest { userId -> userRepository.observeUser(userId) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearSession()
            onLoggedOut()
        }
    }
}
