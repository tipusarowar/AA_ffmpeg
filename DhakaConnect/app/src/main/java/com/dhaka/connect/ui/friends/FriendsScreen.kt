package com.dhaka.connect.ui.friends

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dhaka.connect.data.local.entities.UserEntity
import com.dhaka.connect.ui.components.AvatarImage
import com.dhaka.connect.ui.rememberAppContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(onOpenProfile: (String) -> Unit) {
    val container = rememberAppContainer()
    val viewModel: FriendsViewModel = viewModel(
        factory = viewModelFactory {
            initializer { FriendsViewModel(container.socialRepository, container.userRepository, container.sessionManager) }
        }
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Friends") }) }) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (uiState.incomingRequests.isNotEmpty()) {
                item { SectionHeader("Friend requests") }
                items(uiState.incomingRequests, key = { it.friendship.id }) { request ->
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AvatarImage(url = request.requester.avatarUrl, modifier = Modifier.clickable { onOpenProfile(request.requester.id) })
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(request.requester.fullName, style = MaterialTheme.typography.titleMedium)
                                Text(request.requester.area, style = MaterialTheme.typography.labelSmall)
                            }
                            Column {
                                Button(onClick = { viewModel.acceptRequest(request) }) { Text("Accept") }
                                Spacer(modifier = Modifier.width(4.dp))
                                OutlinedButton(onClick = { viewModel.declineRequest(request) }) { Text("Decline") }
                            }
                        }
                    }
                }
            }

            if (uiState.friends.isNotEmpty()) {
                item { SectionHeader("Your friends (${uiState.friends.size})") }
                items(uiState.friends, key = { it.id }) { friend ->
                    UserRow(user = friend, onClick = { onOpenProfile(friend.id) })
                }
            }

            if (uiState.suggestions.isNotEmpty()) {
                item { SectionHeader("People you may know in Dhaka") }
                items(uiState.suggestions, key = { it.id }) { suggestion ->
                    SuggestionRow(
                        user = suggestion,
                        onOpenProfile = { onOpenProfile(suggestion.id) },
                        onAddFriend = { viewModel.sendFriendRequest(suggestion.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun UserRow(user: UserEntity, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarImage(url = user.avatarUrl)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(user.fullName, style = MaterialTheme.typography.titleMedium)
            Text(user.area, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SuggestionRow(user: UserEntity, onOpenProfile: () -> Unit, onAddFriend: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.clickable(onClick = onOpenProfile), verticalAlignment = Alignment.CenterVertically) {
            AvatarImage(url = user.avatarUrl)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(user.fullName, style = MaterialTheme.typography.titleMedium)
                Text(user.area, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        OutlinedButton(onClick = onAddFriend) { Text("Add Friend") }
    }
}
