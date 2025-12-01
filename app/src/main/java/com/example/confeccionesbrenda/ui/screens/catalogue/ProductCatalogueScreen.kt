package com.example.confeccionesbrenda.ui.screens.catalogue

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.confeccionesbrenda.data.models.Product
import com.example.confeccionesbrenda.viewmodel.CatalogueViewModel

/**
 * Pantalla que muestra el catálogo de productos.
 * Ahora RECIBE el ViewModel, no lo crea. Esto asegura que usa la instancia única de la app.
 */
@Composable
fun ProductCatalogueScreen(
    viewModel: CatalogueViewModel,
    onProductClick: (String) -> Unit
) {
    val products by viewModel.products.collectAsState()
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    if (products.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
            Text("Cargando productos...", modifier = Modifier.padding(top = 60.dp))
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(products, key = { _, product -> product.id }) { index, product ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(durationMillis = 500, delayMillis = index * 100)) +
                          slideInVertically(
                              initialOffsetY = { it / 2 },
                              animationSpec = tween(durationMillis = 500, delayMillis = index * 100)
                          )
                ) {
                    ProductCard(product = product, onProductClick = onProductClick, onAddToCartClick = {
                        viewModel.addProductToCart(product.id)
                    })
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onProductClick: (String) -> Unit,
    onAddToCartClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick(product.id.toString()) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = product.description, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$${product.price}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Button(onClick = onAddToCartClick) {
                    Text("Agregar al Carrito")
                }
            }
        }
    }
}
