package com.example.confeccionesbrenda.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

// --- ENTIDADES DE LA BASE DE DATOS ---

/**
 * Representa un usuario registrado en la aplicación.
 * En un proyecto real, la contraseña se guardaría hasheada, no en texto plano.
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val email: String, // El email es la clave primaria, es único.
    val name: String,
    val passwordHash: String // Placeholder para la contraseña
)

/**
 * Representa un producto en la base de datos.
 */
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val stock: Int
)

/**
 * Representa un item dentro del carrito de compras.
 */
@Entity(
    tableName = "shopping_cart",
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = ["id"],
        childColumns = ["productId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["productId"])]
)
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: Int,
    var quantity: Int
)

// --- CLASES DE RELACIÓN (PARA CONSULTAS) ---

/**
 * Clase de datos para combinar un `CartItem` con su `Product` correspondiente.
 */
data class CartItemWithProduct(
    @Embedded
    val cartItem: CartItem,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: Product
)
