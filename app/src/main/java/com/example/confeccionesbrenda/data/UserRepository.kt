package com.example.confeccionesbrenda.data

import com.example.confeccionesbrenda.data.models.User
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que gestiona todos los datos y la lógica de autenticación del usuario.
 * Ahora utiliza tanto el DAO de usuario (para la BD) como las UserPrefs (para la sesión activa).
 */
class UserRepository(private val prefs: UserPrefs, private val userDao: UserDao) {

    // --- Flujos de Datos Públicos (no cambian) ---
    val isLoggedIn: Flow<Boolean> = prefs.isLoggedInFlow
    val name: Flow<String> = prefs.nameFlow
    val email: Flow<String> = prefs.emailFlow
    val profileImageUri: Flow<String> = prefs.profileImageUriFlow

    /**
     * Registra un nuevo usuario.
     * 1. Inserta el usuario en la base de datos de Room.
     * 2. Si la inserción es exitosa, guarda la sesión en DataStore.
     * @return `true` si el registro fue exitoso, `false` si el usuario ya existía.
     */
    suspend fun register(name: String, email: String, password: String): Boolean {
        // En una app real, aquí se generaría un hash seguro de la contraseña.
        val passwordHash = password // Simulación
        val newUser = User(email = email, name = name, passwordHash = passwordHash)

        // Comprobar si el usuario ya existe
        if (userDao.findUserByEmail(email) != null) {
            return false // El email ya está registrado
        }

        userDao.insertUser(newUser)
        prefs.saveSession(name, email)
        return true
    }

    /**
     * Intenta iniciar sesión con un usuario.
     * 1. Busca al usuario por email en la base de datos.
     * 2. Si se encuentra y la contraseña coincide, guarda la sesión.
     * @return `true` si el login es exitoso, `false` si no.
     */
    suspend fun login(email: String, password: String): Boolean {
        val user = userDao.findUserByEmail(email)
        if (user != null) {
            // Aquí se compararía el hash de la contraseña, no el texto plano.
            if (user.passwordHash == password) { // Simulación de validación de contraseña
                prefs.saveSession(user.name, user.email)
                return true
            }
        }
        return false // Usuario no encontrado o contraseña incorrecta
    }

    /**
     * Guarda la URI de la imagen de perfil del usuario.
     */
    suspend fun saveProfileImage(uri: String) {
        prefs.saveProfileImage(uri)
    }

    /**
     * Limpia todos los datos de la sesión del usuario al cerrar sesión.
     */
    suspend fun logout() = prefs.clearSession()
}
