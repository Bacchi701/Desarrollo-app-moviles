package com.example.confeccionesbrenda.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.confeccionesbrenda.domain.Validators
import com.example.confeccionesbrenda.ui.components.PasswordTextField
import com.example.confeccionesbrenda.ui.components.ValidatedTextField
import com.example.confeccionesbrenda.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        ValidatedTextField(value = name, onValueChange = { name = it }, label = "Nombre")
        Spacer(modifier = Modifier.height(16.dp))
        ValidatedTextField(value = email, onValueChange = { email = it }, label = "Email")
        Spacer(modifier = Modifier.height(16.dp))
        PasswordTextField(value = password, onValueChange = { password = it }, label = "Contraseña")
        Spacer(modifier = Modifier.height(16.dp))
        PasswordTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirmar Contraseña")

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (!Validators.passwordsMatch(password, confirmPassword)) {
                    error = "Las contraseñas no coinciden."
                    return@Button
                }
                if (!Validators.isValidPassword(password)) {
                    error = "La contraseña no es lo suficientemente segura."
                    return@Button
                }
                coroutineScope.launch {
                    val success = authViewModel.register(name, email, password)
                    if (success) {
                        onRegisterSuccess()
                    } else {
                        error = "El email ya está registrado."
                        Toast.makeText(context, "Error: El email ya está en uso", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }

        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes cuenta? Inicia Sesión")
        }
    }
}
