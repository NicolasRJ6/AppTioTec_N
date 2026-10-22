package com.example.apptiotec_n.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.apptiotec_n.model.Respuesta

@Dao
interface RespuestaDao {

    @Insert
    suspend fun insertarTodas(respuestas: List<Respuesta>)

    @Query("SELECT * FROM respuesta WHERE evaluacionId = :evaluacionId")
    suspend fun obtenerDeEvaluacion(evaluacionId: Long): List<Respuesta>
}
