package com.dhaka.connect.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dhaka.connect.data.local.entities.NotificationEntity
import com.dhaka.connect.data.local.entities.NotificationType
import com.dhaka.connect.ui.components.AvatarImage
import com.dhaka.connect.ui.rememberAppContainer
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(onNavigateBack: () -> Unit, onOpenPost: (String) -> Unit, onOpenProfile: (String) -> Unit) {
    val container = rememberAppContainer()
    val viewModel: NotificationsViewModel = viewModel(
        factory = viewModelFactory { initializer { NotificationsViewModel(container.socialRepository, container.sessionManager) } }
    )
    val notifications by viewModel.notifications.collectAsState()

    LaunchedEffect(Unit) { viewModel.markAllRead() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            items(notifications, key = { it.id }) { notification ->
                NotificationRow(
                    notification = notification,
                    onClick = {
                        when (notification.type) {
                            NotificationType.LIKE, NotificationType.COMMENT ->
                                notification.relatedId?.let(onOpenPost)
                            NotificationType.FRIEND_REQUEST, NotificationType.FRIEND_ACCEPTED ->
                                notification.relatedId?.let(onOpenProfile)
                            NotificationType.GROUP_JOIN -> Unit
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun NotificationRow(notification: NotificationEntity, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (notification.isRead) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarImage(url = notification.actorAvatar, size = 40.dp)
        Spacer(modifier = Modifier.width(10.dp))
        androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f)) {
            Text("${notification.actorName} ${notification.message}", style = MaterialTheme.typography.bodyMedium)
            Text(
                DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(notification.createdAt)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
