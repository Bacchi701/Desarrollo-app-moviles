package com.example.confeccionesbrenda.data

import com.example.confeccionesbrenda.data.models.CartItem
import com.example.confeccionesbrenda.data.models.CartItemWithProduct
import com.example.confeccionesbrenda.data.models.Product
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para gestionar los datos de Productos y el Carrito de Compras.
 * Abstrae la fuente de datos (en este caso, Room) del resto de la aplicación (ViewModels).
 */
class ProductRepository(private val productDao: ProductDao) {

    /**
     * Expone un Flow con la lista de todos los productos.
     */
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    /**
     * Expone un Flow con la lista de items del carrito enriquecidos con la info del producto.
     * Esta es la fuente de datos principal para la pantalla del carrito.
     */
    val cartItemsWithProducts: Flow<List<CartItemWithProduct>> = productDao.getCartItemsWithProducts()

    /**
     * Obtiene un producto específico por su ID.
     */
    fun getProductById(productId: Int): Flow<Product?> {
        return productDao.getProductById(productId)
    }

    /**
     * Lógica para añadir un producto al carrito.
     * Si el producto ya está en el carrito, incrementa la cantidad.
     * Si no está, lo inserta como un nuevo item.
     */
    suspend fun addProductToCart(productId: Int) {
        val existingItem = productDao.getCartItemByProductId(productId)

        if (existingItem != null) {
            // Si el item ya existe, incrementamos la cantidad
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            productDao.updateCartItem(updatedItem)
        } else {
            // Si no existe, creamos un nuevo item con cantidad 1
            val newItem = CartItem(productId = productId, quantity = 1)
            productDao.insertCartItem(newItem)
        }
    }

    /**
     * Actualiza la cantidad de un item en el carrito.
     * Si la nueva cantidad es 0 o menos, elimina el item.
     */
    suspend fun updateCartItemQuantity(productId: Int, newQuantity: Int) {
        val existingItem = productDao.getCartItemByProductId(productId)
        if (existingItem != null) {
            if (newQuantity > 0) {
                val updatedItem = existingItem.copy(quantity = newQuantity)
                productDao.updateCartItem(updatedItem)
            } else {
                // Si la cantidad es 0 o menos, eliminamos el item del carrito
                productDao.deleteCartItemById(existingItem.id)
            }
        }
    }

    /**
     * Elimina completamente un producto (y todas sus cantidades) del carrito.
     */
    suspend fun removeProductFromCart(productId: Int) {
        val existingItem = productDao.getCartItemByProductId(productId)
        if (existingItem != null) {
            productDao.deleteCartItemById(existingItem.id)
        }
    }

    /**
     * Vacía completamente el carrito de compras.
     */
    suspend fun clearCart() {
        productDao.clearCart()
    }
}
