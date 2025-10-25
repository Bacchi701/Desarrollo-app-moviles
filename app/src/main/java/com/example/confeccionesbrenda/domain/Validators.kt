package com.example.confeccionesbrenda.domain

import android.util.Patterns

/**
 * Lógica de validaciones desacoplada de la vista
 */
object Validators {

    fun isValidName(name: String): Boolean =
        name.trim().length >= 3

    fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isValidPassword(pass: String): Boolean {
        // 6+ caracteres con al menos 1 número y 1 mayúscula
        val hasNumber = pass.any { it.isDigit() }
        val hasUpper = pass.any { it.isUpperCase() }
        return pass.length >= 6 && hasNumber && hasUpper
    }

    fun passwordsMatch(p1: String, p2: String): Boolean = p1 == p2
}
