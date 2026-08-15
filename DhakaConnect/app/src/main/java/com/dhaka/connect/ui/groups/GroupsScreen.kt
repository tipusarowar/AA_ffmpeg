package com.dhaka.connect.ui.groups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.dhaka.connect.data.local.dao.GroupWithMembership
import com.dhaka.connect.ui.rememberAppContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen(onOpenGroup: (String) -> Unit) {
    val container = rememberAppContainer()
    val viewModel: GroupsViewModel = viewModel(
        factory = viewModelFactory { initializer { GroupsViewModel(container.groupRepository, container.sessionManager) } }
    )
    val groups by viewModel.groups.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Dhaka Groups") }) }) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            items(groups, key = { it.id }) { group ->
                GroupCard(
                    group = group,
                    onClick = { onOpenGroup(group.id) },
                    onToggleMembership = { viewModel.toggleMembership(group) }
                )
            }
        }
    }
}

@Composable
private fun GroupCard(group: GroupWithMembership, onClick: () -> Unit, onToggleMembership: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
    ) {
        Column {
            AsyncImage(
                model = group.coverUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(group.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "${group.area} · ${group.memberCount} members",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (group.isMember) {
                    TextButton(onClick = onToggleMembership) { Text("Joined") }
                } else {
                    OutlinedButton(onClick = onToggleMembership) { Text("Join") }
                }
            }
        }
    }
}
