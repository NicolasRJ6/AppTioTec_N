package com.example.apptiotec_n.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsejoDao {

    @Query("SELECT * FROM consejos ORDER BY id")
    fun observarTodos(): Flow<List<ConsejoEntity>>

    @Query("SELECT MAX(actualizadoEn) FROM consejos")
    fun observarUltimaActualizacion(): Flow<Long?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarTodos(consejos: List<ConsejoEntity>)

    @Query("DELETE FROM consejos")
    suspend fun limpiar()

    @Transaction
    suspend fun reemplazarTodos(consejos: List<ConsejoEntity>) {
        limpiar()
        guardarTodos(consejos)
    }
}
