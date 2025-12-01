package com.example.confeccionesbrenda.data

import android.util.Log
import com.example.confeccionesbrenda.data.models.CartItemWithProduct
import com.example.confeccionesbrenda.data.models.Product
import com.example.confeccionesbrenda.data.network.RetrofitClient
import com.example.confeccionesbrenda.data.network.mappers.toDomainModel
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio actualizado para gestionar productos desde una fuente local (Room)
 * y una remota (API).
 */
class ProductRepository(private val productDao: ProductDao) {

    /**
     * Expone un Flow con la lista de productos DESDE LA BASE DE DATOS LOCAL.
     * La UI siempre observará esta fuente de verdad única.
     */
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    /**
     * Sincroniza el catálogo de productos.
     * 1. Llama a la API para obtener los productos más recientes.
     * 2. Mapea los DTOs de la red a las entidades de la base de datos.
     * 3. Inserta los nuevos productos en la base de datos local (Room).
     * La UI se actualizará automáticamente gracias al Flow de `allProducts`.
     */
    suspend fun refreshProducts() {
        try {
            // 1. Obtener datos de la red
            val apiResponse = RetrofitClient.instance.getProducts()
            val networkProducts = apiResponse.products

            // 2. Mapear a modelo de dominio/base de datos
            val domainProducts = networkProducts.toDomainModel()

            // 3. Insertar en la base de datos local
            productDao.insertProducts(domainProducts)
            Log.d("ProductRepository", "Productos sincronizados exitosamente desde la API.")
        } catch (e: Exception) {
            // En una app real, aquí manejaríamos los errores de red de forma más elegante
            // (ej. mostrando un Toast o un Snackbar, o logueando a un servicio de errores).
            Log.e("ProductRepository", "Error al sincronizar productos: ${e.message}")
        }
    }
    
    // --- El resto de las funciones (carrito, etc.) se mantienen igual ---
    
    val cartItemsWithProducts: Flow<List<CartItemWithProduct>> = productDao.getCartItemsWithProducts()

    fun getProductById(productId: Int): Flow<Product?> {
        return productDao.getProductById(productId)
    }

    suspend fun addProductToCart(productId: Int) {
        val existingItem = productDao.getCartItemByProductId(productId)
        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            productDao.updateCartItem(updatedItem)
        } else {
            val newItem = com.example.confeccionesbrenda.data.models.CartItem(productId = productId, quantity = 1)
            productDao.insertCartItem(newItem)
        }
    }

    suspend fun updateCartItemQuantity(productId: Int, newQuantity: Int) {
        val existingItem = productDao.getCartItemByProductId(productId)
        if (existingItem != null) {
            if (newQuantity > 0) {
                val updatedItem = existingItem.copy(quantity = newQuantity)
                productDao.updateCartItem(updatedItem)
            } else {
                productDao.deleteCartItemById(existingItem.id)
            }
        }
    }

    suspend fun removeProductFromCart(productId: Int) {
        val existingItem = productDao.getCartItemByProductId(productId)
        if (existingItem != null) {
            productDao.deleteCartItemById(existingItem.id)
        }
    }

    suspend fun clearCart() {
        productDao.clearCart()
    }
}
