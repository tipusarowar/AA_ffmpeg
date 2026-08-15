package com.dhaka.connect.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.dhaka.connect.AppContainer
import com.dhaka.connect.DhakaConnectApp

/** Resolves the app-wide [AppContainer] so screens can build their ViewModels without a DI framework. */
@Composable
fun rememberAppContainer(): AppContainer {
    val context = LocalContext.current.applicationContext as DhakaConnectApp
    return context.container
}
