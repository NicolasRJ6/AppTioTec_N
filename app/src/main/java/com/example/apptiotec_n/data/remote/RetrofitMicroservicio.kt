package com.example.apptiotec_n.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente HTTP del microservicio Spring Boot de TioTec.
 *
 * Antes de usar la pantalla "Consejos del microservicio" el backend debe estar
 * en ejecución (`mvn spring-boot:run` dentro de `microservicio/`).
 *
 * BASE_URL por defecto usa `127.0.0.1` y el reenvío de puerto de adb:
 *
 * ```
 * adb reverse tcp:8080 tcp:8080
 * ```
 *
 * Alternativas si el teléfono no está conectado por USB:
 * - Misma red Wi-Fi: reemplazar por la IP del computador, por ejemplo
 *   `http://192.168.1.11:8080/`.
 * - Emulador de Android Studio: `http://10.0.2.2:8080/`.
 */
object RetrofitMicroservicio {

    private const val BASE_URL = "http://127.0.0.1:8080/"

    val api: ConsejoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ConsejoApiService::class.java)
    }
}
