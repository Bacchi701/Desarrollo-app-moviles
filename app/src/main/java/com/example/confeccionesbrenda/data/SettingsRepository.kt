package com.example.confeccionesbrenda.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Se crea una instancia de DataStore a nivel de archivo.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Repositorio para gestionar las preferencias de configuración de la app, como el tema.
 * Utiliza Jetpack DataStore para persistir los datos de forma asíncrona.
 */
class SettingsRepository(context: Context) {

    private val dataStore = context.dataStore

    // Se define una clave para la preferencia del tema oscuro.
    private object PreferencesKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    /**
     * Flow que emite `true` si el modo oscuro está activado, `false` en caso contrario.
     * La UI observará este Flow para reaccionar a los cambios de tema.
     */
    val isDarkMode: Flow<Boolean> = dataStore.data.map {
        preferences ->
        preferences[PreferencesKeys.IS_DARK_MODE] ?: false // Valor por defecto es `false` (tema claro)
    }

    /**
     * Función `suspend` para actualizar la preferencia del modo oscuro.
     * @param isDarkMode El nuevo valor para la preferencia.
     */
    suspend fun setDarkMode(isDarkMode: Boolean) {
        dataStore.edit {
            preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] = isDarkMode
        }
    }
}
