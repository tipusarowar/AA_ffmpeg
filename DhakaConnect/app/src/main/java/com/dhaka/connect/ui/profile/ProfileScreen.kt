package com.dhaka.connect.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import com.dhaka.connect.ui.components.AvatarImage
import com.dhaka.connect.ui.components.PostCard
import com.dhaka.connect.ui.rememberAppContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: String,
    onNavigateBack: () -> Unit,
    onOpenComments: (String) -> Unit,
    onOpenChat: (String) -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val container = rememberAppContainer()
    val viewModel: ProfileViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                ProfileViewModel(userId, container.userRepository, container.postRepository, container.socialRepository, container.sessionManager)
            }
        }
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.viewedUser?.fullName.orEmpty()) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        val user = uiState.viewedUser
        if (uiState.isLoading || user == null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                item {
                    Column {
                        AsyncImage(
                            model = user.coverUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().height(140.dp)
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            AvatarImage(url = user.avatarUrl, size = 84.dp)
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(user.fullName, style = MaterialTheme.typography.titleLarge)
                            Text(
                                user.area,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (user.bio.isNotBlank()) {
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(user.bio, style = MaterialTheme.typography.bodyMedium)
                            }

                            Spacer(modifier = Modifier.padding(8.dp))
                            ProfileActions(
                                uiState = uiState,
                                onAddFriend = viewModel::sendFriendRequest,
                                onMessage = { onOpenChat(userId) },
                                onEditProfile = onEditProfile,
                                onLogout = { viewModel.logout(onLogout) }
                            )
                        }
                    }
                }
                items(uiState.posts, key = { it.id }) { post ->
                    PostCard(
                        post = post,
                        onOpenProfile = {},
                        onToggleLike = {},
                        onOpenComments = { onOpenComments(post.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileActions(
    uiState: ProfileUiState,
    onAddFriend: () -> Unit,
    onMessage: () -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    if (uiState.isOwnProfile) {
        Row {
            Button(onClick = onEditProfile, modifier = Modifier.weight(1f, fill = false)) { Text("Edit Profile") }
            Spacer(modifier = Modifier.padding(4.dp))
            OutlinedButton(onClick = onLogout) { Text("Log Out") }
        }
    } else {
        Row {
            when (uiState.relation) {
                FriendRelation.FRIENDS -> OutlinedButton(onClick = {}, enabled = false) { Text("Friends") }
                FriendRelation.REQUEST_SENT -> OutlinedButton(onClick = {}, enabled = false) { Text("Request Sent") }
                FriendRelation.REQUEST_RECEIVED -> OutlinedButton(onClick = {}, enabled = false) { Text("Respond in Friends tab") }
                else -> Button(onClick = onAddFriend) { Text("Add Friend") }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            OutlinedButton(onClick = onMessage) { Text("Message") }
        }
    }
}
