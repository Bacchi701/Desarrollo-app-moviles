package com.example.confeccionesbrenda.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// --------------- DataStore ---------------
// Extension property para obtener un DataStore de tipo Preferences.
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

// Claves de las preferencias
private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
private val KEY_NAME = stringPreferencesKey("name")
private val KEY_EMAIL = stringPreferencesKey("email")

/**
 * Fuente de datos local para leer/escribir la sesión del usuario.
 */

class UserPrefs(private val context: Context) {

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data.map { it[KEY_LOGGED_IN] ?: false }
    val nameFlow: Flow<String> = context.dataStore.data.map { it[KEY_NAME] ?: "" }
    val emailFlow: Flow<String> = context.dataStore.data.map { it[KEY_EMAIL] ?: "" }

    suspend fun saveSession(name: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = true
            prefs[KEY_NAME] = name
            prefs[KEY_EMAIL] = email
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = false
            prefs[KEY_NAME] = ""
            prefs[KEY_EMAIL] = ""
        }
    }
}