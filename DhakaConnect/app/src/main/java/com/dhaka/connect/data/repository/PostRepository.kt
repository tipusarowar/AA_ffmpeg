package com.dhaka.connect.data.repository

import com.dhaka.connect.data.local.dao.CommentWithAuthor
import com.dhaka.connect.data.local.dao.PostDao
import com.dhaka.connect.data.local.dao.PostFeedItem
import com.dhaka.connect.data.local.entities.CommentEntity
import com.dhaka.connect.data.local.entities.PostEntity
import com.dhaka.connect.data.local.entities.PostLikeEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class PostRepository(private val postDao: PostDao) {

    fun feed(currentUserId: String): Flow<List<PostFeedItem>> = postDao.observeFeed(currentUserId)

    fun groupFeed(groupId: String, currentUserId: String): Flow<List<PostFeedItem>> =
        postDao.observeGroupFeed(groupId, currentUserId)

    fun userPosts(authorId: String, currentUserId: String): Flow<List<PostFeedItem>> =
        postDao.observeUserPosts(authorId, currentUserId)

    fun post(postId: String, currentUserId: String): Flow<PostFeedItem?> =
        postDao.observePost(postId, currentUserId)

    fun comments(postId: String): Flow<List<CommentWithAuthor>> = postDao.observeComments(postId)

    suspend fun createPost(authorId: String, content: String, imageUrl: String?, area: String?, groupId: String? = null) {
        postDao.insertPost(
            PostEntity(
                id = UUID.randomUUID().toString(),
                authorId = authorId,
                content = content,
                imageUrl = imageUrl,
                area = area,
                groupId = groupId,
                baseLikeCount = 0,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun toggleLike(postId: String, userId: String) {
        val existing = postDao.getLike(postId, userId)
        if (existing != null) {
            postDao.unlike(postId, userId)
        } else {
            postDao.like(PostLikeEntity(postId, userId))
        }
    }

    suspend fun addComment(postId: String, authorId: String, content: String) {
        postDao.insertComment(
            CommentEntity(
                id = UUID.randomUUID().toString(),
                postId = postId,
                authorId = authorId,
                content = content,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun getPostAuthor(postId: String): String? = postDao.getPostAuthor(postId)
}
