package com.dhaka.connect.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dhaka.connect.data.local.entities.CommentEntity
import com.dhaka.connect.data.local.entities.PostEntity
import com.dhaka.connect.data.local.entities.PostLikeEntity
import kotlinx.coroutines.flow.Flow

data class PostFeedItem(
    val id: String,
    val authorId: String,
    val authorName: String,
    val authorAvatar: String,
    val content: String,
    val imageUrl: String?,
    val area: String?,
    val groupId: String?,
    val groupName: String?,
    val createdAt: Long,
    val baseLikeCount: Int,
    val commentCount: Int,
    val likedByMe: Boolean
)

private const val FEED_ITEM_SELECT = """
    SELECT p.id as id, p.authorId as authorId, u.fullName as authorName, u.avatarUrl as authorAvatar,
           p.content as content, p.imageUrl as imageUrl, p.area as area, p.groupId as groupId,
           g.name as groupName,
           p.createdAt as createdAt, p.baseLikeCount as baseLikeCount,
           (SELECT COUNT(*) FROM comments c WHERE c.postId = p.id) as commentCount,
           EXISTS(SELECT 1 FROM post_likes pl WHERE pl.postId = p.id AND pl.userId = :currentUserId) as likedByMe
    FROM posts p
    JOIN users u ON u.id = p.authorId
    LEFT JOIN groups g ON g.id = p.groupId
"""

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("$FEED_ITEM_SELECT WHERE p.groupId IS NULL ORDER BY p.createdAt DESC")
    fun observeFeed(currentUserId: String): Flow<List<PostFeedItem>>

    @Query("$FEED_ITEM_SELECT WHERE p.groupId = :groupId ORDER BY p.createdAt DESC")
    fun observeGroupFeed(groupId: String, currentUserId: String): Flow<List<PostFeedItem>>

    @Query("$FEED_ITEM_SELECT WHERE p.authorId = :authorId ORDER BY p.createdAt DESC")
    fun observeUserPosts(authorId: String, currentUserId: String): Flow<List<PostFeedItem>>

    @Query("$FEED_ITEM_SELECT WHERE p.id = :postId LIMIT 1")
    fun observePost(postId: String, currentUserId: String): Flow<PostFeedItem?>

    @Query("SELECT * FROM post_likes WHERE postId = :postId AND userId = :userId LIMIT 1")
    suspend fun getLike(postId: String, userId: String): PostLikeEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun like(like: PostLikeEntity)

    @Query("DELETE FROM post_likes WHERE postId = :postId AND userId = :userId")
    suspend fun unlike(postId: String, userId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    @Query(
        """
        SELECT c.id, c.postId, c.authorId, c.content, c.createdAt,
               u.fullName as authorName, u.avatarUrl as authorAvatar
        FROM comments c
        JOIN users u ON u.id = c.authorId
        WHERE c.postId = :postId
        ORDER BY c.createdAt ASC
        """
    )
    fun observeComments(postId: String): Flow<List<CommentWithAuthor>>

    @Query("SELECT authorId FROM posts WHERE id = :postId LIMIT 1")
    suspend fun getPostAuthor(postId: String): String?

    @Delete
    suspend fun deletePost(post: PostEntity)
}

data class CommentWithAuthor(
    val id: String,
    val postId: String,
    val authorId: String,
    val content: String,
    val createdAt: Long,
    val authorName: String,
    val authorAvatar: String
)
