package com.example.apptiotec_n.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.apptiotec_n.model.Accion
import kotlinx.coroutines.flow.Flow

@Dao
interface AccionDao {

    @Insert
    suspend fun insertarTodas(acciones: List<Accion>)

    @Query("SELECT * FROM accion WHERE evaluacionId = :evaluacionId ORDER BY id")
    fun observarDeEvaluacion(evaluacionId: Long): Flow<List<Accion>>

    @Query("UPDATE accion SET completada = :completada WHERE id = :id")
    suspend fun actualizarCompletada(id: Long, completada: Boolean)
}
