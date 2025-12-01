package com.example.confeccionesbrenda.ui.screens

// ... (todos los imports se mantienen igual)
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.confeccionesbrenda.viewmodel.AuthViewModel


/**
 * Pantalla de Perfil de Usuario.
 * AHORA RECIBE EL VIEWMODEL COMO PARÁMETRO, NO LO CREA.
 * Esto asegura que usa la instancia única y estable de la app.
 */
@Composable
fun PerfilScreen(authViewModel: AuthViewModel) { // ¡IMPORTANTE! Se eliminó la creación del ViewModel aquí.
    val context = LocalContext.current
    // Los estados se recolectan del ViewModel que se recibe como parámetro.
    val profileImageUri by authViewModel.profileImageUri.collectAsState()
    val userName by authViewModel.name.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permiso concedido
            } else {
                // Permiso denegado
            }
        }
    )

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { authViewModel.saveProfileImage(it.toString()) }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Mi Perfil", style = MaterialTheme.typography.headlineMedium)

        Box {
            Image(
                painter = rememberAsyncImagePainter(
                    model = if (profileImageUri.isNotEmpty()) Uri.parse(profileImageUri) else "https://www.gravatar.com/avatar/?d=mp"
                ),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentScale = ContentScale.Crop
            )
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Cambiar foto",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Text(userName, style = MaterialTheme.typography.titleLarge)

        Button(onClick = {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // Si ya hay permiso, se podría lanzar la cámara directamente.
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }) {
            Text("Solicitar Permiso de Cámara")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
