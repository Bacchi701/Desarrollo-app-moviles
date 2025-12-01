package com.example.confeccionesbrenda.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.confeccionesbrenda.R
import com.example.confeccionesbrenda.ui.components.ImageCarousel

/**
 * Pantalla principal de la aplicación (Home).
 * Muestra un saludo de bienvenida y un carrusel de imágenes.
 * Ya no necesita el parámetro `onLogout` porque se ha centralizado en la `TopAppBar`.
 *
 * @param userName El nombre del usuario logueado.
 */
@Composable
fun HomeScreen(userName: String) { // Parámetro onLogout eliminado
    // Lista de recursos de imagen para el carrusel.
    val images = listOf(
        R.drawable.carousel_1,
        R.drawable.carousel_2,
        R.drawable.carousel_3
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mensaje de bienvenida personalizado.
        Text(
            text = "¡Bienvenido, $userName!",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.Start)
        )

        // Título para la sección del carrusel.
        Text(
            text = "Novedades y Promociones",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        // Componente del carrusel de imágenes.
        ImageCarousel(images = images, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.weight(1f)) // Empuja los botones hacia abajo

        Button(
            onClick = { /* Podría navegar a una pantalla de ofertas especiales */ },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Ver Ofertas Especiales") }
    }
}
