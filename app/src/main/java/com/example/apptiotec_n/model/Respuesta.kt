package com.example.apptiotec_n.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "respuesta")
data class Respuesta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val evaluacionId: Long,
    val preguntaId: Int,
    val categoria: String,
    val valor: Int
)
