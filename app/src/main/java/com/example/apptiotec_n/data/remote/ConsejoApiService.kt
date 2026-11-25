package com.example.apptiotec_n.data.remote

import com.example.apptiotec_n.data.model.ConsejoServidor
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Endpoints del microservicio de consejos (Spring Boot).
 *
 * Cubre el CRUD completo: listar, crear, actualizar y eliminar.
 */
interface ConsejoApiService {

    @GET("api/consejos")
    suspend fun listar(): List<ConsejoServidor>

    @POST("api/consejos")
    suspend fun crear(@Body consejo: ConsejoServidor): ConsejoServidor

    @PUT("api/consejos/{id}")
    suspend fun actualizar(
        @Path("id") id: Long,
        @Body consejo: ConsejoServidor
    ): ConsejoServidor

    @DELETE("api/consejos/{id}")
    suspend fun eliminar(@Path("id") id: Long)
}
