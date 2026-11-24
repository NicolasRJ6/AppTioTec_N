package com.example.apptiotec_n.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente HTTP único para la API REST de consejos.
 *
 * Se usa una sola instancia de Retrofit para reutilizar la conexión y evitar
 * crear clientes nuevos en cada pantalla.
 */
object RetrofitInstance {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
