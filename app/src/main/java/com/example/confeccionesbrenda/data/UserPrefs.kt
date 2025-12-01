package com.example.confeccionesbrenda.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property para obtener un DataStore de tipo Preferences.
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

// --- Claves para las Preferencias ---
private object PrefKeys {
    val LOGGED_IN = booleanPreferencesKey("logged_in")
    val NAME = stringPreferencesKey("name")
    val EMAIL = stringPreferencesKey("email")
    // Nueva clave para la imagen de perfil
    val PROFILE_IMAGE_URI = stringPreferencesKey("profile_image_uri")
}

/**
 * Clase que gestiona la persistencia de los datos de sesión del usuario usando DataStore.
 */
class UserPrefs(private val context: Context) {

    // --- Flujos de Datos (Flows) para observar las preferencias ---

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data.map { it[PrefKeys.LOGGED_IN] ?: false }
    val nameFlow: Flow<String> = context.dataStore.data.map { it[PrefKeys.NAME] ?: "" }
    val emailFlow: Flow<String> = context.dataStore.data.map { it[PrefKeys.EMAIL] ?: "" }
    // Nuevo Flow para la imagen de perfil
    val profileImageUriFlow: Flow<String> = context.dataStore.data.map { it[PrefKeys.PROFILE_IMAGE_URI] ?: "" }

    // --- Funciones para modificar las preferencias ---

    suspend fun saveSession(name: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[PrefKeys.LOGGED_IN] = true
            prefs[PrefKeys.NAME] = name
            prefs[PrefKeys.EMAIL] = email
        }
    }

    /**
     * Guarda únicamente la URI de la imagen de perfil.
     */
    suspend fun saveProfileImage(uri: String) {
        context.dataStore.edit { prefs ->
            prefs[PrefKeys.PROFILE_IMAGE_URI] = uri
        }
    }

    /**
     * Limpia todos los datos de la sesión del usuario.
     */
    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs[PrefKeys.LOGGED_IN] = false
            prefs[PrefKeys.NAME] = ""
            prefs[PrefKeys.EMAIL] = ""
            prefs[PrefKeys.PROFILE_IMAGE_URI] = "" // Limpiar también la URI
        }
    }
}
