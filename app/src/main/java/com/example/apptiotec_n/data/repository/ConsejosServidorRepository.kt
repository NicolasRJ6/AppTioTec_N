package com.example.apptiotec_n.data.repository

import com.example.apptiotec_n.data.model.ConsejoServidor
import com.example.apptiotec_n.data.remote.ConsejoApiService
import com.example.apptiotec_n.data.remote.RetrofitMicroservicio

/**
 * Acceso a los consejos guardados en el microservicio Spring Boot.
 *
 * Aquí el servidor es la fuente de verdad: no se guarda copia en Room, así que
 * lo que muestra la pantalla es siempre lo que hay en la base de datos H2.
 */
class ConsejosServidorRepository(
    private val api: ConsejoApiService = RetrofitMicroservicio.api
) {

    suspend fun listar(): List<ConsejoServidor> = api.listar()

    suspend fun crear(consejo: ConsejoServidor): ConsejoServidor =
        api.crear(consejo)

    suspend fun actualizar(consejo: ConsejoServidor): ConsejoServidor =
        api.actualizar(consejo.id, consejo)

    suspend fun eliminar(id: Long) = api.eliminar(id)
}
