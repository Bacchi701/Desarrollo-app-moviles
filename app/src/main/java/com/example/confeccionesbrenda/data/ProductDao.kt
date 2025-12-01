package com.example.confeccionesbrenda.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.confeccionesbrenda.data.models.CartItem
import com.example.confeccionesbrenda.data.models.CartItemWithProduct
import com.example.confeccionesbrenda.data.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductById(productId: Int): Flow<Product?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Transaction
    @Query("SELECT * FROM shopping_cart")
    fun getCartItemsWithProducts(): Flow<List<CartItemWithProduct>>

    @Query("SELECT * FROM shopping_cart WHERE productId = :productId")
    suspend fun getCartItemByProductId(productId: Int): CartItem?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCartItem(item: CartItem)

    @Update
    suspend fun updateCartItem(item: CartItem)

    @Query("DELETE FROM shopping_cart WHERE id = :itemId")
    suspend fun deleteCartItemById(itemId: Int)

    @Query("DELETE FROM shopping_cart")
    suspend fun clearCart()

    /**
     * Nueva función para contar los productos. Es útil para saber si la BD está poblada.
     */
    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int
}
