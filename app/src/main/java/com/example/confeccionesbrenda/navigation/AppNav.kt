package com.example.confeccionesbrenda.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.confeccionesbrenda.data.UserPrefs
import com.example.confeccionesbrenda.data.UserRepository
import com.example.confeccionesbrenda.ui.screens.*
import com.example.confeccionesbrenda.viewmodel.AuthViewModel
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Info


sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Main : Screen("main")
}

sealed class BottomDest(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    data object Home : BottomDest("home", "Home", Icons.Filled.Home)
    data object Perfil : BottomDest("perfil", "Perfil", Icons.Filled.Person)
    data object Config : BottomDest("config", "Config", Icons.Filled.Settings)
    data object Acerca : BottomDest("acerca", "Acerca", Icons.Filled.Info)
}

/**
 * Navegacion principal de la app
 */
@Composable
fun AppNav() {
    val rootNav = rememberNavController()

    val ctx = LocalContext.current
    val repo = UserRepository(UserPrefs(ctx))
    val vm = AuthViewModel(repo)

    val isLoggedIn = vm.isLoggedIn.collectAsState(initial = false)

    NavHost(
        navController = rootNav,
        startDestination = if (isLoggedIn.value) Screen.Main.route else Screen.Login.route
    ) {
        // LOGIN
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { email ->
                    vm.login(email)
                    rootNav.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { rootNav.navigate(Screen.Register.route) }
            )
        }
        // REGISTER
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { name, email ->
                    vm.register(name, email)
                    rootNav.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                // Volver al login
                onNavigateToLogin = {
                    rootNav.popBackStack()
                }
            )
        }

        // Contenedor con bottom bar y sub-navegacion
        composable(Screen.Main.route) {
            MainWithBottomBar(vm = vm)
        }
    }
}

/**
 * Contenedor con bottom Navigation interno.
 * Tiene su propio NavController para las pestañas home, perfil, config y acerca de.
 */
@Composable
private fun MainWithBottomBar(vm: AuthViewModel) {
    val navController = rememberNavController()
    val userName by vm.name.collectAsState(initial = "")

    val items = listOf(BottomDest.Home, BottomDest.Perfil, BottomDest.Config, BottomDest.Acerca)

    // barra inferior con botones
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val current = navBackStackEntry?.destination?.route
                items.forEach { dest ->
                    NavigationBarItem(
                        selected = current == dest.route,
                        onClick = {
                            navController.navigate(dest.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(dest.icon, contentDescription = dest.label) },
                        label = { Text(dest.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BottomDest.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            // TAB: HOME
            composable(BottomDest.Home.route) {
                HomeScreen(
                    userName = userName.ifBlank { "Cliente" },
                    onLogout = { vm.logout() }
                )
            }
            // TAB: PERFIL
            composable(BottomDest.Perfil.route) { PerfilScreen() }
            // TAB: CONFIG
            composable(BottomDest.Config.route) { ConfigScreen() }
            // TAB: ACERCA DE
            composable(BottomDest.Acerca.route) { AcercaDeScreen() }
        }
    }
}