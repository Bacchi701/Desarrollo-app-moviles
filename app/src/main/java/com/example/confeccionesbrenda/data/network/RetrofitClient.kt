package com.example.confeccionesbrenda.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto Singleton para gestionar la instancia de Retrofit.
 * Esto asegura que solo se cree una instancia del cliente de red en toda la app,
 * lo cual es eficiente en términos de recursos.
 */
object RetrofitClient {

    // URL base de la API de donde obtendremos los datos de los productos.
    private const val BASE_URL = "https://dummyjson.com/"

    /**
     * Propiedad `lazy` que crea la instancia de ApiService.
     * `lazy` significa que el código dentro del bloque solo se ejecutará la primera vez
     * que se acceda a la propiedad, y el resultado se guardará en caché para usos futuros.
     */
    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // Se establece la URL base para todas las peticiones.
            .addConverterFactory(GsonConverterFactory.create()) // Se añade el conversor para transformar JSON a objetos Kotlin.
            .build()

        // Retrofit crea una implementación de nuestra interfaz ApiService.
        retrofit.create(ApiService::class.java)
    }
}
