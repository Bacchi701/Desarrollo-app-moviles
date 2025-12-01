package com.example.confeccionesbrenda.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.confeccionesbrenda.data.SettingsRepository
import com.example.confeccionesbrenda.viewmodel.ThemeViewModel
import com.example.confeccionesbrenda.viewmodel.ThemeViewModelFactory

// Función de utilidad para obtener la instancia del ViewModel.
@Composable
private fun themeViewModel(context: Context): ThemeViewModel {
    val repository = SettingsRepository(context)
    val factory = ThemeViewModelFactory(repository)
    return viewModel(factory = factory)
}

/**
 * Pantalla de Configuración.
 * Permite al usuario modificar preferencias de la aplicación, como el tema.
 */
@Composable
fun ConfigScreen() {
    val viewModel: ThemeViewModel = themeViewModel(LocalContext.current)
    // Se observa el estado del tema (oscuro/claro).
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Configuración", style = MaterialTheme.typography.headlineSmall)
        
        // Fila para la opción de modo oscuro.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Modo Oscuro", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = isDarkMode,
                onCheckedChange = { viewModel.setDarkMode(it) }
            )
        }
    }
}
