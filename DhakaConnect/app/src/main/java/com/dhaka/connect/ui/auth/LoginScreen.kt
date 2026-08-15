package com.dhaka.connect.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.KeyboardType
import androidx.compose.ui.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dhaka.connect.ui.rememberAppContainer

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit) {
    val container = rememberAppContainer()
    val viewModel: AuthViewModel = viewModel(
        factory = viewModelFactory { initializer { AuthViewModel(container.userRepository, container.sessionManager) } }
    )
    val uiState by viewModel.uiState.collectAsState()

    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("ঢাকা কানেক্ট", style = MaterialTheme.typography.titleLarge)
        Text("Connect with your neighborhood in Dhaka", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone number") },
            placeholder = { Text("01XXXXXXXXX") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(6.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        uiState.errorMessage?.let { message ->
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(4.dp))
            Text(message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(10.dp))
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.login(phone, password, onLoginSuccess) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log In")
            }
        }

        TextButton(onClick = onNavigateToRegister) {
            Text("New to DhakaConnect? Create an account")
        }

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(8.dp))
        Text(
            "Demo account: 01711000000 / dhaka123",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
