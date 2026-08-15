package com.dhaka.connect.ui.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhaka.connect.data.local.dao.GroupWithMembership
import com.dhaka.connect.data.repository.GroupRepository
import com.dhaka.connect.data.session.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class GroupsViewModel(
    private val groupRepository: GroupRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val currentUserId = sessionManager.currentUserId.filterNotNull()

    val groups: StateFlow<List<GroupWithMembership>> = currentUserId.flatMapLatest { userId ->
        groupRepository.allGroups(userId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleMembership(group: GroupWithMembership) {
        viewModelScope.launch {
            val userId = currentUserId.first()
            if (group.isMember) {
                groupRepository.leaveGroup(group.id, userId)
            } else {
                groupRepository.joinGroup(group.id, userId)
            }
        }
    }
}
