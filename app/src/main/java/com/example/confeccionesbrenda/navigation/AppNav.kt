package com.example.confeccionesbrenda.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.confeccionesbrenda.ui.screens.*
import com.example.confeccionesbrenda.ui.screens.catalogue.ProductCatalogueScreen
import com.example.confeccionesbrenda.ui.screens.catalogue.ProductDetailScreen
import com.example.confeccionesbrenda.ui.screens.catalogue.ShoppingCartScreen
import com.example.confeccionesbrenda.viewmodel.AuthViewModel
import com.example.confeccionesbrenda.viewmodel.CatalogueViewModel
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Main : Screen("main")
    data object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    data object ShoppingCart : Screen("shopping_cart")
}

sealed class BottomDest(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    data object Home : BottomDest("home", "Home", Icons.Filled.Home)
    data object Catalog : BottomDest("catalog", "Catálogo", Icons.Filled.Store)
    data object Perfil : BottomDest("perfil", "Perfil", Icons.Filled.Person)
    data object Config : BottomDest("config", "Config", Icons.Filled.Settings)
    data object Acerca : BottomDest("acerca", "Acerca", Icons.Filled.Info)
}

@Composable
fun AppNav(authViewModel: AuthViewModel, catalogueViewModel: CatalogueViewModel) {
    val rootNav = rememberNavController()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    
    if (isLoggedIn) {
        MainContentNav(authViewModel = authViewModel, catalogueViewModel = catalogueViewModel, rootNavController = rootNav)
    } else {
        AuthNav(authViewModel = authViewModel, rootNavController = rootNav)
    }
}

@Composable
private fun AuthNav(authViewModel: AuthViewModel, rootNavController: NavHostController) {
    NavHost(navController = rootNavController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { /* No-op */ }, 
                onNavigateToRegister = { rootNavController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = { /* No-op */ },
                onNavigateToLogin = { rootNavController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // <-- ANOTACIÓN AÑADIDA AQUÍ
@Composable
private fun MainContentNav(authViewModel: AuthViewModel, catalogueViewModel: CatalogueViewModel, rootNavController: NavHostController) {
    val bottomNavController = rememberNavController()

    NavHost(navController = rootNavController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Confecciones Brenda") },
                        actions = {
                            IconButton(onClick = { rootNavController.navigate(Screen.ShoppingCart.route) }) {
                                Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                            }
                            IconButton(onClick = { authViewModel.logout() }) {
                                Icon(Icons.Filled.Logout, contentDescription = "Cerrar Sesión")
                            }
                        }
                    )
                },
                bottomBar = {
                    val bottomBarItems = listOf(BottomDest.Home, BottomDest.Catalog, BottomDest.Perfil, BottomDest.Config, BottomDest.Acerca)
                    NavigationBar {
                        val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        bottomBarItems.forEach { dest ->
                            NavigationBarItem(
                                selected = currentRoute == dest.route,
                                onClick = {
                                    bottomNavController.navigate(dest.route) {
                                        popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
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
                    navController = bottomNavController,
                    startDestination = BottomDest.Home.route,
                    modifier = Modifier.padding(padding)
                ) {
                    composable(BottomDest.Home.route) {
                        val userName by authViewModel.name.collectAsState()
                        HomeScreen(userName = userName.ifBlank { "Cliente" })
                    }
                    composable(BottomDest.Catalog.route) {
                        ProductCatalogueScreen(
                            viewModel = catalogueViewModel,
                            onProductClick = { productId ->
                                rootNavController.navigate(Screen.ProductDetail.createRoute(productId))
                            }
                        )
                    }
                    composable(BottomDest.Perfil.route) {
                        PerfilScreen(authViewModel = authViewModel)
                    }
                    composable(BottomDest.Config.route) { ConfigScreen() }
                    composable(BottomDest.Acerca.route) { AcercaDeScreen() }
                }
            }
        }
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            requireNotNull(productId) { "Product ID no puede ser nulo" }
            ProductDetailScreen(
                productId = productId,
                viewModel = catalogueViewModel,
                onBack = { rootNavController.popBackStack() }
            )
        }
        composable(Screen.ShoppingCart.route) {
            ShoppingCartScreen(
                viewModel = catalogueViewModel,
                onBack = { rootNavController.popBackStack() }
            )
        }
    }
}
