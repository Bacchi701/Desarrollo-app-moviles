package com.example.confeccionesbrenda.domain

import android.util.Patterns

/**
 * Objeto singleton que contiene la lógica de validación de formularios de la aplicación.
 * Al desacoplar esta lógica en su propio objeto, la hacemos reutilizable y fácil de testear,
 * siguiendo los principios de una arquitectura limpia.
 */
object Validators {

    /**
     * Valida que el nombre de usuario no esté vacío y tenga al menos 3 caracteres.
     * `trim()` se usa para eliminar espacios en blanco al principio y al final.
     * @param name El nombre a validar.
     * @return `true` si el nombre es válido, `false` en caso contrario.
     */
    fun isValidName(name: String): Boolean {
        return name.trim().length >= 3
    }

    /**
     * Valida que el formato del email sea correcto.
     * Utiliza el patrón estándar de Android `Patterns.EMAIL_ADDRESS` que es robusto y está bien probado.
     * @param email El email a validar.
     * @return `true` si el email tiene un formato válido, `false` en caso contrario.
     */
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Valida la fortaleza de la contraseña según las siguientes reglas:
     * 1. Mínimo 6 caracteres de longitud.
     * 2. Debe contener al menos un número.
     * 3. Debe contener al menos una letra mayúscula.
     * @param pass La contraseña a validar.
     * @return `true` si la contraseña cumple con las reglas, `false` en caso contrario.
     */
    fun isValidPassword(pass: String): Boolean {
        val hasNumber = pass.any { it.isDigit() }
        val hasUpper = pass.any { it.isUpperCase() }
        return pass.length >= 6 && hasNumber && hasUpper
    }

    /**
     * Compara dos contraseñas para asegurar que coincidan.
     * Esencial para los formularios de registro con campo de confirmación de contraseña.
     * @param p1 La primera contraseña.
     * @param p2 La segunda contraseña (confirmación).
     * @return `true` si ambas contraseñas son idénticas, `false` en caso contrario.
     */
    fun passwordsMatch(p1: String, p2: String): Boolean {
        return p1 == p2
    }
}
