package com.example.apptiotec_n.data.model

/**
 * Consejo tal como lo entrega el microservicio Spring Boot (`/api/consejos`).
 *
 * Se mantiene separado de [Consejo] porque la API externa usa los campos
 * `title` y `body`, mientras que el microservicio usa `titulo` y `descripcion`.
 */
data class ConsejoServidor(
    val id: Long = 0L,
    val titulo: String = "",
    val descripcion: String = ""
)
