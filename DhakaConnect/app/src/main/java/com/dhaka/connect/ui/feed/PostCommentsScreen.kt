package com.dhaka.connect.ui.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dhaka.connect.data.local.dao.CommentWithAuthor
import com.dhaka.connect.ui.components.AvatarImage
import com.dhaka.connect.ui.components.PostCard
import com.dhaka.connect.ui.rememberAppContainer
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCommentsScreen(postId: String, onNavigateBack: () -> Unit, onOpenProfile: (String) -> Unit) {
    val container = rememberAppContainer()
    val viewModel: CommentsViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                CommentsViewModel(postId, container.postRepository, container.socialRepository, container.userRepository, container.sessionManager)
            }
        }
    )
    val uiState by viewModel.uiState.collectAsState()
    var draft by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = draft,
                    onValueChange = { draft = it },
                    placeholder = { Text("Write a comment...") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = {
                    viewModel.addComment(draft)
                    draft = ""
                }) {
                    Icon(Icons.Filled.Send, contentDescription = "Send")
                }
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading || uiState.post == null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxWidth()) {
                item {
                    PostCard(
                        post = uiState.post!!,
                        onOpenProfile = onOpenProfile,
                        onToggleLike = { viewModel.toggleLike() },
                        onOpenComments = {}
                    )
                }
                items(uiState.comments, key = { it.id }) { comment ->
                    CommentRow(comment = comment, onOpenProfile = onOpenProfile)
                }
            }
        }
    }
}

@Composable
private fun CommentRow(comment: CommentWithAuthor, onOpenProfile: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onOpenProfile(comment.authorId) }
    ) {
        AvatarImage(url = comment.authorAvatar, size = 32.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(comment.authorName, style = MaterialTheme.typography.titleMedium)
            Text(comment.content, style = MaterialTheme.typography.bodyMedium)
            Text(
                DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(comment.createdAt)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
