package com.dhaka.connect.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dhaka.connect.ui.components.AvatarImage
import com.dhaka.connect.ui.components.PostCard
import com.dhaka.connect.ui.rememberAppContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onOpenComments: (String) -> Unit,
    onOpenProfile: (String) -> Unit,
    onCreatePost: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenMessages: () -> Unit
) {
    val container = rememberAppContainer()
    val viewModel: FeedViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                FeedViewModel(container.postRepository, container.socialRepository, container.userRepository, container.sessionManager)
            }
        }
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ঢাকা কানেক্ট", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = onOpenSearch) { Icon(Icons.Filled.Search, contentDescription = "Search") }
                    IconButton(onClick = onOpenMessages) { Icon(Icons.Filled.Chat, contentDescription = "Messages") }
                    IconButton(onClick = onOpenNotifications) {
                        if (uiState.unreadNotifications > 0) {
                            BadgedBox(badge = { Badge { Text(uiState.unreadNotifications.toString()) } }) {
                                Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                            }
                        } else {
                            Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxWidth()) {
            item {
                CreatePostPrompt(
                    avatarUrl = uiState.currentUser?.avatarUrl.orEmpty(),
                    onClick = onCreatePost
                )
            }
            items(uiState.posts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    onOpenProfile = onOpenProfile,
                    onToggleLike = { viewModel.toggleLike(post) },
                    onOpenComments = { onOpenComments(post.id) }
                )
            }
        }
    }
}

@Composable
private fun CreatePostPrompt(avatarUrl: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (avatarUrl.isNotBlank()) {
                AvatarImage(url = avatarUrl, size = 36.dp)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(20.dp))
                    .padding(vertical = 10.dp, horizontal = 14.dp)
            ) {
                Text("What's on your mind, Dhaka?", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
