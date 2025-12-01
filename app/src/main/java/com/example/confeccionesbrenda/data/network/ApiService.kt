package com.example.confeccionesbrenda.data.network

import com.example.confeccionesbrenda.data.network.dto.ProductsApiResponse
import retrofit2.http.GET

/**
 * Interfaz que define los endpoints de la API.
 * Retrofit usará esta interfaz para generar el código de red necesario.
 */
interface ApiService {

    /**
     * Realiza una petición GET al endpoint "/products".
     * La función se marca como `suspend` para poder ser llamada desde una corrutina.
     * @return Un objeto `ProductsApiResponse` que contiene la lista de productos y la paginación.
     */
    @GET("products")
    suspend fun getProducts(): ProductsApiResponse
}
