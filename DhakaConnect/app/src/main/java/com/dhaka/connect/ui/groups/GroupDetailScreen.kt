package com.dhaka.connect.ui.groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.dhaka.connect.ui.components.PostCard
import com.dhaka.connect.ui.rememberAppContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    groupId: String,
    onNavigateBack: () -> Unit,
    onOpenComments: (String) -> Unit,
    onOpenProfile: (String) -> Unit,
    onCreatePost: () -> Unit
) {
    val container = rememberAppContainer()
    val viewModel: GroupDetailViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                GroupDetailViewModel(groupId, container.groupRepository, container.postRepository, container.sessionManager)
            }
        }
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.group?.name.orEmpty()) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        val group = uiState.group
        if (uiState.isLoading || group == null) {
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
                            model = group.coverUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().height(160.dp)
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(group.name, style = MaterialTheme.typography.titleLarge)
                            Text(
                                "${group.area} · ${group.memberCount} members",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(4.dp))
                            Text(group.description, style = MaterialTheme.typography.bodyMedium)
                            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(6.dp))
                            if (group.isMember) {
                                Button(onClick = onCreatePost, modifier = Modifier.fillMaxWidth()) { Text("Post to group") }
                                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(2.dp))
                                OutlinedButton(onClick = { viewModel.toggleMembership() }, modifier = Modifier.fillMaxWidth()) {
                                    Text("Leave group")
                                }
                            } else {
                                Button(onClick = { viewModel.toggleMembership() }, modifier = Modifier.fillMaxWidth()) {
                                    Text("Join group")
                                }
                            }
                        }
                    }
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
}
