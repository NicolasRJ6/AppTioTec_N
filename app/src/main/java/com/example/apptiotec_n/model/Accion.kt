package com.example.apptiotec_n.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accion")
data class Accion(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val evaluacionId: Long,
    val texto: String,
    val completada: Boolean = false
)
