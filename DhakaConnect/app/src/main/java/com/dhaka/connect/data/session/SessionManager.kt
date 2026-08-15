package com.dhaka.connect.data.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "dhaka_connect_session")

class SessionManager(private val context: Context) {

    private val currentUserIdKey = stringPreferencesKey("current_user_id")

    val currentUserId: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[currentUserIdKey]
    }

    suspend fun setCurrentUser(userId: String) {
        context.dataStore.edit { prefs -> prefs[currentUserIdKey] = userId }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs -> prefs.remove(currentUserIdKey) }
    }
}
