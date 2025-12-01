package com.example.confeccionesbrenda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.confeccionesbrenda.data.AppDatabase
import com.example.confeccionesbrenda.data.ProductRepository
import com.example.confeccionesbrenda.data.SettingsRepository
import com.example.confeccionesbrenda.data.UserPrefs
import com.example.confeccionesbrenda.data.UserRepository
import com.example.confeccionesbrenda.navigation.AppNav
import com.example.confeccionesbrenda.ui.theme.ConfeccionesBrendaTheme
import com.example.confeccionesbrenda.viewmodel.AuthViewModel
import com.example.confeccionesbrenda.viewmodel.CatalogueViewModel
import com.example.confeccionesbrenda.viewmodel.ThemeViewModel

class MainActivity : ComponentActivity() {

    // 1. Se crea la instancia ÚNICA de la base de datos.
    private val database by lazy { AppDatabase.getDatabase(applicationContext) }

    // 2. Se crean las instancias ÚNICAS de todos los repositorios.
    private val userRepository by lazy { UserRepository(UserPrefs(applicationContext), database.userDao()) }
    private val productRepository by lazy { ProductRepository(database.productDao()) }
    private val settingsRepository by lazy { SettingsRepository(applicationContext) }

    // 3. Se crea una Factory ÚNICA que sabe cómo construir todos los ViewModels.
    private val viewModelFactory by lazy {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when {
                    modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(userRepository) as T
                    modelClass.isAssignableFrom(ThemeViewModel::class.java) -> ThemeViewModel(settingsRepository) as T
                    modelClass.isAssignableFrom(CatalogueViewModel::class.java) -> CatalogueViewModel(productRepository) as T
                    else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
        }
    }

    // 4. Se obtienen instancias ÚNICAS de los ViewModels usando la factory.
    private val themeViewModel: ThemeViewModel by viewModels { viewModelFactory }
    private val authViewModel: AuthViewModel by viewModels { viewModelFactory }
    private val catalogueViewModel: CatalogueViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkMode by themeViewModel.isDarkMode.collectAsState()

            ConfeccionesBrendaTheme(darkTheme = isDarkMode) {
                // 5. Se inyectan las instancias ÚNICAS a la navegación.
                AppNav(authViewModel = authViewModel, catalogueViewModel = catalogueViewModel)
            }
        }
    }
}
