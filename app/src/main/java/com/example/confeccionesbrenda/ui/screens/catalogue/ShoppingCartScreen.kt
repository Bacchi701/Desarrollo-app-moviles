package com.example.confeccionesbrenda.ui.screens.catalogue

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.confeccionesbrenda.data.models.CartItemWithProduct
import com.example.confeccionesbrenda.viewmodel.CatalogueViewModel

/**
 * Pantalla del Carrito de Compras.
 * Ahora RECIBE el ViewModel, no lo crea.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCartScreen(
    viewModel: CatalogueViewModel,
    onBack: () -> Unit
) {
    val cartItems by viewModel.cartItemsWithProducts.collectAsState()
    val totalPrice = cartItems.sumOf { it.product.price * it.cartItem.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío")
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemView(item = item, viewModel = viewModel)
                    }
                }
                CartSummary(totalPrice = totalPrice, onCheckout = {
                    viewModel.clearCart()
                })
            }
        }
    }
}

@Composable
private fun CartItemView(item: CartItemWithProduct, viewModel: CatalogueViewModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.product.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "$${item.product.price}", fontWeight = FontWeight.Bold)
            }
            QuantityControl(item = item, viewModel = viewModel)
            IconButton(onClick = { viewModel.removeProductFromCart(item.product.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar item")
            }
        }
    }
}

@Composable
private fun QuantityControl(item: CartItemWithProduct, viewModel: CatalogueViewModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { 
            viewModel.updateCartItemQuantity(item.product.id, item.cartItem.quantity - 1)
        }) {
            Text("-")
        }
        Text("${item.cartItem.quantity}")
        IconButton(onClick = { 
            viewModel.updateCartItemQuantity(item.product.id, item.cartItem.quantity + 1)
        }) {
            Text("+")
        }
    }
}

@Composable
private fun CartSummary(totalPrice: Double, onCheckout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total:", style = MaterialTheme.typography.headlineSmall)
            Text("$${totalPrice}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onCheckout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finalizar Compra")
        }
    }
}
