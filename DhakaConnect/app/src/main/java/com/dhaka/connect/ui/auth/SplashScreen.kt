package com.dhaka.connect.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dhaka.connect.ui.rememberAppContainer
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit, onNavigateToHome: () -> Unit) {
    val container = rememberAppContainer()

    LaunchedEffect(Unit) {
        val userId = container.sessionManager.currentUserId.first()
        if (userId != null) onNavigateToHome() else onNavigateToLogin()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ঢাকা কানেক্ট", style = MaterialTheme.typography.titleLarge)
            Text("DhakaConnect", style = MaterialTheme.typography.titleMedium)
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(12.dp))
            CircularProgressIndicator()
        }
    }
}
