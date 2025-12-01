package com.example.confeccionesbrenda.data.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) para la respuesta de la API de productos.
 * Representa la estructura del JSON que recibimos de la red.
 * Usamos @SerializedName para mapear los nombres de los campos del JSON a nuestras propiedades,
 * lo que nos da flexibilidad si los nombres no coinciden con las convenciones de Kotlin.
 */
data class ProductsApiResponse(
    val products: List<ProductDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

data class ProductDto(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    @SerializedName("images") // El campo en el JSON se llama "images", lo mapeamos a "imageUrls"
    val imageUrls: List<String>
)
