package com.example.confeccionesbrenda.data.network.mappers

import com.example.confeccionesbrenda.data.models.Product
import com.example.confeccionesbrenda.data.network.dto.ProductDto

/**
 * Función de extensión para convertir un objeto de transferencia de datos (DTO) de la red
 * a una entidad de la base de datos local (Product).
 * Esto desacopla la capa de red de la capa de dominio/base de datos.
 */
fun ProductDto.toDomainModel(): Product {
    return Product(
        // La API de dummyjson.com no nos sirve el ID de producto como un UUID, así que usaremos el que nos da.
        // En un caso real, podríamos querer generar nuestro propio ID.
        id = this.id,
        name = this.title,
        description = this.description,
        price = this.price,
        // La API nos devuelve una URL completa, pero podríamos querer solo el nombre del archivo.
        // Para este ejemplo, usaremos la miniatura (thumbnail) que nos provee.
        imageUrl = this.thumbnail, 
        stock = this.stock
    )
}

/**
 * Función de extensión para convertir una lista de DTOs a una lista de entidades de dominio.
 */
fun List<ProductDto>.toDomainModel(): List<Product> {
    return this.map { it.toDomainModel() }
}
