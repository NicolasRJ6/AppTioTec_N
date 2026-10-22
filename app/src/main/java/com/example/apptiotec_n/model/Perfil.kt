package com.example.apptiotec_n.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perfil")
data class Perfil(
    @PrimaryKey val id: Int = 1,
    val alias: String,
    val nivelExperiencia: String,
    val dispositivo: String,
    val sistemaOperativo: String,
    val imagenUri: String? = null
)
