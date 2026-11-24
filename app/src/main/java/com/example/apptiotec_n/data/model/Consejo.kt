package com.example.apptiotec_n.data.model

import com.google.gson.annotations.SerializedName

/**
 * Consejo obtenido desde la API REST de JSONPlaceholder.
 *
 * La API entrega posts con los campos userId, id, title y body. Se mapean a un
 * modelo propio para que la interfaz no dependa del formato externo.
 */
data class Consejo(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("title") val titulo: String = "",
    @SerializedName("body") val descripcion: String = ""
)
