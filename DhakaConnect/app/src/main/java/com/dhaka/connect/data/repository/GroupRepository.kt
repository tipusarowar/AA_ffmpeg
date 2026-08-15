package com.dhaka.connect.data.repository

import com.dhaka.connect.data.local.dao.GroupDao
import com.dhaka.connect.data.local.dao.GroupWithMembership
import com.dhaka.connect.data.local.entities.GroupMemberEntity
import kotlinx.coroutines.flow.Flow

class GroupRepository(private val groupDao: GroupDao) {

    fun allGroups(currentUserId: String): Flow<List<GroupWithMembership>> =
        groupDao.observeGroups(currentUserId)

    fun myGroups(currentUserId: String): Flow<List<GroupWithMembership>> =
        groupDao.observeMyGroups(currentUserId)

    fun group(groupId: String, currentUserId: String): Flow<GroupWithMembership?> =
        groupDao.observeGroup(groupId, currentUserId)

    suspend fun joinGroup(groupId: String, userId: String) {
        groupDao.insertMember(GroupMemberEntity(groupId, userId, System.currentTimeMillis()))
    }

    suspend fun leaveGroup(groupId: String, userId: String) {
        groupDao.removeMember(groupId, userId)
    }
}
