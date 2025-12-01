package com.example.confeccionesbrenda.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Clase de pruebas unitarias para el objeto Validators.
 * El nombre de cada función sigue el patrón: `nombreDeLaFuncion_Condicion_ResultadoEsperado`
 * Se utiliza la librería Google Truth para hacer las aserciones más legibles.
 */
class ValidatorsTest {

    // --- Pruebas para isValidName ---

    @Test
    fun isValidName_nombreCorto_retornaFalse() {
        val resultado = Validators.isValidName("ab")
        assertThat(resultado).isFalse()
    }

    @Test
    fun isValidName_nombreValido_retornaTrue() {
        val resultado = Validators.isValidName("Bryan")
        assertThat(resultado).isTrue()
    }

    @Test
    fun isValidName_nombreConEspacios_retornaTrue() {
        val resultado = Validators.isValidName("  Bryan  ")
        assertThat(resultado).isTrue()
    }

    // --- Pruebas para isValidEmail ---

    @Test
    fun isValidEmail_emailSinArroba_retornaFalse() {
        val resultado = Validators.isValidEmail("bryan.test.com")
        assertThat(resultado).isFalse()
    }

    @Test
    fun isValidEmail_emailSinDominio_retornaFalse() {
        val resultado = Validators.isValidEmail("bryan@")
        assertThat(resultado).isFalse()
    }

    @Test
    fun isValidEmail_emailValido_retornaTrue() {
        val resultado = Validators.isValidEmail("bryan@test.com")
        assertThat(resultado).isTrue()
    }

    // --- Pruebas para isValidPassword ---

    @Test
    fun isValidPassword_passwordCorta_retornaFalse() {
        val resultado = Validators.isValidPassword("Abc1")
        assertThat(resultado).isFalse()
    }

    @Test
    fun isValidPassword_passwordSinNumero_retornaFalse() {
        val resultado = Validators.isValidPassword("Abcdef")
        assertThat(resultado).isFalse()
    }

    @Test
    fun isValidPassword_passwordSinMayuscula_retornaFalse() {
        val resultado = Validators.isValidPassword("abcdef1")
        assertThat(resultado).isFalse()
    }

    @Test
    fun isValidPassword_passwordValida_retornaTrue() {
        val resultado = Validators.isValidPassword("Abcdef1")
        assertThat(resultado).isTrue()
    }

    // --- Pruebas para passwordsMatch ---

    @Test
    fun passwordsMatch_contraseñasDiferentes_retornaFalse() {
        val resultado = Validators.passwordsMatch("Abc1", "abc1")
        assertThat(resultado).isFalse()
    }

    @Test
    fun passwordsMatch_contraseñasIguales_retornaTrue() {
        val resultado = Validators.passwordsMatch("Abcdef1", "Abcdef1")
        assertThat(resultado).isTrue()
    }
}
