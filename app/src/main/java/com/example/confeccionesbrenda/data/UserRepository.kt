package com.example.confeccionesbrenda.data

import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que abstrae la fuente de datos.
 */
class UserRepository(private val prefs: UserPrefs) {

    val isLoggedIn: Flow<Boolean> = prefs.isLoggedInFlow
    val name: Flow<String> = prefs.nameFlow
    val email: Flow<String> = prefs.emailFlow

    // Registrar
    suspend fun register(name: String, email: String) {
        // En un backend, también guardarías password.
        prefs.saveSession(name, email)
    }

    // Login
    suspend fun login(email: String) {
        // Simulación: si hay email válido, guardamos sesión.
        // (Puedes cruzar con DataStore si quisieras validar contra lo guardado)
        prefs.saveSession(name = email.substringBefore("@"), email = email)
    }

    // Logout
    suspend fun logout() = prefs.clearSession()
}
