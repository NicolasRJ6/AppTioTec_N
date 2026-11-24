package com.example.apptiotec_n.data.remote

import com.example.apptiotec_n.data.model.Consejo
import retrofit2.http.GET

interface ApiService {

    @GET("posts")
    suspend fun obtenerConsejos(): List<Consejo>
}
