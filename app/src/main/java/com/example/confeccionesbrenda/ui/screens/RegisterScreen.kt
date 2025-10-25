@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.confeccionesbrenda.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.confeccionesbrenda.domain.Validators
import com.example.confeccionesbrenda.ui.components.PasswordTextField
import com.example.confeccionesbrenda.ui.components.ValidatedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

@Composable
fun RegisterScreen(
    onRegisterSuccess: (name: String, email: String) -> Unit,
    onNavigateToLogin: () -> Unit = {}
) {
    val ctx = LocalContext.current
    val haptics = LocalHapticFeedback.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var pass2 by remember { mutableStateOf("") }

    var nameErr by remember { mutableStateOf<String?>(null) }
    var emailErr by remember { mutableStateOf<String?>(null) }
    var passErr by remember { mutableStateOf<String?>(null) }
    var pass2Err by remember { mutableStateOf<String?>(null) }

    var pickedUri by remember { mutableStateOf<Uri?>(null) }
    var cameraBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val pickPhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> pickedUri = uri }

    val takePhotoPreview = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bmp -> cameraBitmap = bmp }

    fun validate(): Boolean {
        nameErr = if (Validators.isValidName(name)) null else "Nombre muy corto"
        emailErr = if (Validators.isValidEmail(email)) null else "Email inválido"
        passErr = if (Validators.isValidPassword(pass)) null else "Contraseña insegura (6+, número y mayúscula)"
        pass2Err = if (Validators.passwordsMatch(pass, pass2)) null else "Las contraseñas no coinciden"
        return listOf(nameErr, emailErr, passErr, pass2Err).all { it == null }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Registro") }) }
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
                // Botones para imagen de perfil
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(onClick = {
                        pickPhoto.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }) { Text("Elegir foto") }

                    OutlinedButton(onClick = { takePhotoPreview.launch(null) }) {
                        Text("Tomar foto")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Preview por si se tomó foto con la cámara
                cameraBitmap?.let { bmp ->
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(72.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                }

                ValidatedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nombre",
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    errorText = nameErr,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                ValidatedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    errorText = emailErr,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                PasswordTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = "Contraseña",
                    errorText = passErr,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                PasswordTextField(
                    value = pass2,
                    onValueChange = { pass2 = it },
                    label = "Confirmar contraseña",
                    errorText = pass2Err,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        if (validate()) {
                            haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                            onRegisterSuccess(name, email)
                            Toast.makeText(ctx, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) { Text("Crear cuenta") }

                // Enlace para ir a Login
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = onNavigateToLogin) {
                    Text("¿Ya tienes cuenta? Inicia sesión")
                }
            }
        }
    }
}

