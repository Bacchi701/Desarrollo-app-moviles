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

    val products: StateFlow<List<Product>> = repository.allProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    /**
     * StateFlow que expone los items del carrito con la información de sus productos.
     */
    val cartItemsWithProducts: StateFlow<List<CartItemWithProduct>> = repository.cartItemsWithProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun getProductById(productId: Int): Flow<Product?> {
        return repository.getProductById(productId)
    }

    fun addProductToCart(productId: Int) {
        viewModelScope.launch {
            repository.addProductToCart(productId)
        }
    }

    /**
     * Actualiza la cantidad de un producto en el carrito.
     */
    fun updateCartItemQuantity(productId: Int, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateCartItemQuantity(productId, newQuantity)
        }
    }

    /**
     * Elimina un producto del carrito.
     */
    fun removeProductFromCart(productId: Int) {
        viewModelScope.launch {
            repository.removeProductFromCart(productId)
        }
    }

    /**
     * Vacía todo el carrito.
     */
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
