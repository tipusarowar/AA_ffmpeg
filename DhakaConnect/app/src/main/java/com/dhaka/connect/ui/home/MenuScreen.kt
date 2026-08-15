package com.dhaka.connect.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.dhaka.connect.ui.rememberAppContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onOpenProfile: (String) -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenMessages: () -> Unit,
    onLoggedOut: () -> Unit
) {
    val container = rememberAppContainer()
    val viewModel: HomeMenuViewModel = viewModel(
        factory = viewModelFactory { initializer { HomeMenuViewModel(container.userRepository, container.sessionManager) } }
    )
    val currentUser by viewModel.currentUser.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Menu") }) }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            currentUser?.let { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenProfile(user.id) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarImage(url = user.avatarUrl, size = 56.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(user.fullName, style = MaterialTheme.typography.titleMedium)
                        Text(user.area, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                HorizontalDivider()
            }

            MenuItem(icon = Icons.Filled.Notifications, label = "Notifications", onClick = onOpenNotifications)
            MenuItem(icon = Icons.Filled.Chat, label = "Messages", onClick = onOpenMessages)
            HorizontalDivider()
            MenuItem(icon = Icons.Filled.Logout, label = "Log Out", onClick = { viewModel.logout(onLoggedOut) })
        }
    }
}

@Composable
private fun MenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}
