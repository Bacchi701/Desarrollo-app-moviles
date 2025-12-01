package com.example.confeccionesbrenda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.confeccionesbrenda.data.ProductRepository
import com.example.confeccionesbrenda.data.models.CartItemWithProduct
import com.example.confeccionesbrenda.data.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CatalogueViewModel(private val repository: ProductRepository) : ViewModel() {

    /**
     * StateFlow que expone la lista de productos desde la base de datos local.
     * La UI siempre observará esta fuente, que se actualizará cuando la BD cambie.
     */
    val products: StateFlow<List<Product>> = repository.allProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val cartItemsWithProducts: StateFlow<List<CartItemWithProduct>> = repository.cartItemsWithProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    /**
     * Bloque de inicialización.
     * Se ejecuta una sola vez cuando el ViewModel es creado por primera vez.
     * Lanza una corrutina para llamar a la sincronización de productos.
     */
    init {
        refreshProducts()
    }

    /**
     * Expone la función para refrescar el catálogo desde la fuente remota (API).
     */
    fun refreshProducts() {
        viewModelScope.launch {
            repository.refreshProducts()
        }
    }
    
    fun getProductById(productId: Int): Flow<Product?> {
        return repository.getProductById(productId)
    }

    fun addProductToCart(productId: Int) {
        viewModelScope.launch {
            repository.addProductToCart(productId)
        }
    }

    fun updateCartItemQuantity(productId: Int, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateCartItemQuantity(productId, newQuantity)
        }
    }

    fun removeProductFromCart(productId: Int) {
        viewModelScope.launch {
            repository.removeProductFromCart(productId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}

class CatalogueViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatalogueViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatalogueViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
