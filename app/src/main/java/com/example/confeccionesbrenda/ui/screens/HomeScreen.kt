package com.example.confeccionesbrenda.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.confeccionesbrenda.R
import com.example.confeccionesbrenda.ui.components.ImageCarousel

/**
 * Menú principal con carrusel e ítems base del catálogo.
 */
@Composable
fun HomeScreen(
    userName: String,
    onLogout: () -> Unit
) {
    // Imagenes para el carrusel
    val images = listOf(
        R.drawable.carousel_1,
        R.drawable.carousel_2,
        R.drawable.carousel_3
    )

    // Estructura base de la pantalla
    @file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.home_title)) },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Salir")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // saludo de bienvenida
            Text(text = "¡Hola, $userName!", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))

            // carrusel
            ImageCarousel(images = images, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(24.dp))

            // Botones y acciones  del home
            Button(
                onClick = { /* TODO: Navegar a Catálogo */ },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ver catálogo") }

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { /* TODO: Navegar a Ofertas */ },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ofertas de temporada") }
        }
    }
}
