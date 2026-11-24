package com.example.apptiotec_n.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.apptiotec_n.data.model.Consejo

@Entity(tableName = "consejos")
data class ConsejoEntity(
    @PrimaryKey val id: Int,
    val titulo: String,
    val descripcion: String,
    val actualizadoEn: Long
)

fun ConsejoEntity.aModelo(): Consejo = Consejo(
    id = id,
    titulo = titulo,
    descripcion = descripcion
)

fun Consejo.aEntidad(actualizadoEn: Long): ConsejoEntity = ConsejoEntity(
    id = id,
    titulo = titulo,
    descripcion = descripcion,
    actualizadoEn = actualizadoEn
)
