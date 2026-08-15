package com.dhaka.connect.ui.feed

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.menuAnchor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import com.dhaka.connect.data.DhakaAreas
import com.dhaka.connect.ui.rememberAppContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(groupId: String? = null, onPostCreated: () -> Unit, onNavigateBack: () -> Unit) {
    val container = rememberAppContainer()
    val viewModel: CreatePostViewModel = viewModel(
        factory = viewModelFactory { initializer { CreatePostViewModel(container.postRepository, container.sessionManager) } }
    )

    var content by remember { mutableStateOf("") }
    var pickedImage by remember { mutableStateOf<Uri?>(null) }
    var area by remember { mutableStateOf<String?>(null) }
    var areaMenuExpanded by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> pickedImage = uri }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (groupId != null) "Post to group" else "Create post") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.submitPost(content, pickedImage?.toString(), area, groupId, onPostCreated)
                        }
                    ) {
                        Text("Post")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                placeholder = { Text("What's happening in Dhaka?") },
                modifier = Modifier.fillMaxWidth().height(160.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            pickedImage?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(180.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedButton(
                onClick = {
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Image, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (pickedImage == null) "Add photo" else "Change photo")
            }

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = areaMenuExpanded,
                onExpandedChange = { areaMenuExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = area ?: "Tag your area (optional)",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Area") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = areaMenuExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = areaMenuExpanded, onDismissRequest = { areaMenuExpanded = false }) {
                    DropdownMenuItem(text = { Text("No area tag") }, onClick = { area = null; areaMenuExpanded = false })
                    DhakaAreas.all.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                area = option
                                areaMenuExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
