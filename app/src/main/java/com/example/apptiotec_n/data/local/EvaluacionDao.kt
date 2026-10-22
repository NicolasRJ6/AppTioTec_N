package com.example.apptiotec_n.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.apptiotec_n.model.Evaluacion
import kotlinx.coroutines.flow.Flow

@Dao
interface EvaluacionDao {

    @Insert
    suspend fun insertar(evaluacion: Evaluacion): Long

    @Query("SELECT * FROM evaluacion ORDER BY fecha DESC")
    fun observarTodas(): Flow<List<Evaluacion>>

    @Query("SELECT * FROM evaluacion ORDER BY fecha DESC LIMIT 1")
    fun observarUltima(): Flow<Evaluacion?>

    @Query("SELECT * FROM evaluacion WHERE id = :id")
    suspend fun buscarPorId(id: Long): Evaluacion?
}
