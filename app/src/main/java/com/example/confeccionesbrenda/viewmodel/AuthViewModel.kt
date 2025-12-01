package com.example.confeccionesbrenda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.confeccionesbrenda.data.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: UserRepository) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = repo.isLoggedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )
    val name: StateFlow<String> = repo.name.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    val email: StateFlow<String> = repo.email.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    val profileImageUri: StateFlow<String> = repo.profileImageUri.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    /**
     * Registra un nuevo usuario.
     * @return `true` si fue exitoso, `false` si el usuario ya existía.
     */
    suspend fun register(name: String, email: String, password: String): Boolean {
        return repo.register(name, email, password)
    }

    /**
     * Intenta iniciar sesión.
     * @return `true` si el login fue exitoso, `false` en caso contrario.
     */
    suspend fun login(email: String, password: String): Boolean {
        return repo.login(email, password)
    }

    fun logout() {
        viewModelScope.launch { repo.logout() }
    }

    fun saveProfileImage(uri: String) {
        viewModelScope.launch { repo.saveProfileImage(uri) }
    }

    companion object {
        fun Factory(repo: UserRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AuthViewModel(repo) as T
                }
            }
        }
    }
}
