package com.example.apptiotec_n.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "evaluacion")
data class Evaluacion(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fecha: Long,
    val puntaje: Int,
    val riesgo: String
)
