package com.dhaka.connect.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dhaka.connect.data.local.dao.PostFeedItem
import java.text.DateFormat
import java.util.Date

@Composable
fun PostCard(
    post: PostFeedItem,
    onOpenProfile: (String) -> Unit,
    onToggleLike: () -> Unit,
    onOpenComments: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenProfile(post.authorId) }
            ) {
                AvatarImage(url = post.authorAvatar, size = 40.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(post.authorName, style = MaterialTheme.typography.titleMedium)
                    val subtitle = buildString {
                        append(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(post.createdAt)))
                        post.groupName?.let { append(" · $it") }
                        post.area?.let { append(" · $it") }
                    }
                    Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            if (post.content.isNotBlank()) {
                Spacer(modifier = Modifier.padding(top = 8.dp))
                Text(post.content, style = MaterialTheme.typography.bodyLarge)
            }

            post.imageUrl?.let { imageUrl ->
                Spacer(modifier = Modifier.padding(top = 8.dp))
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }

            val likeCount = post.baseLikeCount + if (post.likedByMe) 1 else 0
            Spacer(modifier = Modifier.padding(top = 6.dp))
            Text(
                "$likeCount likes · ${post.commentCount} comments",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onToggleLike() }) {
                    IconButton(onClick = onToggleLike) {
                        Icon(
                            imageVector = if (post.likedByMe) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.likedByMe) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text("Like")
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onOpenComments() }) {
                    IconButton(onClick = onOpenComments) {
                        Icon(Icons.Filled.ChatBubbleOutline, contentDescription = "Comment")
                    }
                    Text("Comment")
                }
            }
        }
    }
}
