@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.confeccionesbrenda.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.confeccionesbrenda.domain.Validators
import com.example.confeccionesbrenda.ui.components.PasswordTextField
import com.example.confeccionesbrenda.ui.components.ValidatedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun LoginScreen(
    onLoginSuccess: (email: String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val ctx = LocalContext.current
    val haptics = LocalHapticFeedback.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        emailError = if (Validators.isValidEmail(email)) null else "Email inválido"
        passError = if (Validators.isValidPassword(password)) null else "Contraseña insegura (6+, número y mayúscula)"
        return emailError == null && passError == null
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Iniciar sesión") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .widthIn(max = 520.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ValidatedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    errorText = emailError,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                PasswordTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Contraseña",
                    errorText = passError,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        if (validate()) {
                            haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                            onLoginSuccess(email)
                            Toast.makeText(ctx, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) { Text("Iniciar sesión") }

                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = onNavigateToRegister,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "¿No tienes cuenta? Regístrate",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

