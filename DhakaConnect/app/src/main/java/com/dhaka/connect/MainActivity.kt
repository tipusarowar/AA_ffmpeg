package com.dhaka.connect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dhaka.connect.ui.navigation.DhakaNavGraph
import com.dhaka.connect.ui.theme.DhakaConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DhakaConnectRoot()
        }
    }
}

@Composable
private fun DhakaConnectRoot() {
    DhakaConnectTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            DhakaNavGraph()
        }
    }
}
