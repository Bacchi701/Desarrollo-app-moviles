package com.example.confeccionesbrenda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.confeccionesbrenda.data.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * VM de autenticación (gestiona estado y llama al repositorio).
 */
class AuthViewModel(private val repo: UserRepository) : ViewModel() {

    val isLoggedIn: Flow<Boolean> = repo.isLoggedIn
    val name: Flow<String> = repo.name
    val email: Flow<String> = repo.email

    fun register(name: String, email: String) {
        viewModelScope.launch { repo.register(name, email) }
    }

    fun login(email: String) {
        viewModelScope.launch { repo.login(email) }
    }

    fun logout() {
        viewModelScope.launch { repo.logout() }
    }
}
