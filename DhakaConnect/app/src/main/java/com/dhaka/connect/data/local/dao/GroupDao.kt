package com.dhaka.connect.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dhaka.connect.data.local.entities.GroupEntity
import com.dhaka.connect.data.local.entities.GroupMemberEntity
import kotlinx.coroutines.flow.Flow

data class GroupWithMembership(
    val id: String,
    val name: String,
    val description: String,
    val area: String,
    val coverUrl: String,
    val createdAt: Long,
    val memberCount: Int,
    val isMember: Boolean
)

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<GroupEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: GroupMemberEntity)

    @Query("DELETE FROM group_members WHERE groupId = :groupId AND userId = :userId")
    suspend fun removeMember(groupId: String, userId: String)

    @Query(
        """
        SELECT g.id as id, g.name as name, g.description as description, g.area as area,
               g.coverUrl as coverUrl, g.createdAt as createdAt,
               (SELECT COUNT(*) FROM group_members gm WHERE gm.groupId = g.id) as memberCount,
               EXISTS(SELECT 1 FROM group_members gm2 WHERE gm2.groupId = g.id AND gm2.userId = :currentUserId) as isMember
        FROM groups g
        ORDER BY g.name ASC
        """
    )
    fun observeGroups(currentUserId: String): Flow<List<GroupWithMembership>>

    @Query(
        """
        SELECT g.id as id, g.name as name, g.description as description, g.area as area,
               g.coverUrl as coverUrl, g.createdAt as createdAt,
               (SELECT COUNT(*) FROM group_members gm WHERE gm.groupId = g.id) as memberCount,
               1 as isMember
        FROM groups g
        JOIN group_members gm3 ON gm3.groupId = g.id AND gm3.userId = :currentUserId
        ORDER BY g.name ASC
        """
    )
    fun observeMyGroups(currentUserId: String): Flow<List<GroupWithMembership>>

    @Query(
        """
        SELECT g.id as id, g.name as name, g.description as description, g.area as area,
               g.coverUrl as coverUrl, g.createdAt as createdAt,
               (SELECT COUNT(*) FROM group_members gm WHERE gm.groupId = g.id) as memberCount,
               EXISTS(SELECT 1 FROM group_members gm2 WHERE gm2.groupId = g.id AND gm2.userId = :currentUserId) as isMember
        FROM groups g
        WHERE g.id = :groupId
        LIMIT 1
        """
    )
    fun observeGroup(groupId: String, currentUserId: String): Flow<GroupWithMembership?>
}
