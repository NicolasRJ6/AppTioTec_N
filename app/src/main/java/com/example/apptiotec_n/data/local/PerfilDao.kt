package com.example.apptiotec_n.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apptiotec_n.model.Perfil
import kotlinx.coroutines.flow.Flow

@Dao
interface PerfilDao {

    @Query("SELECT * FROM perfil WHERE id = 1")
    fun observarPerfil(): Flow<Perfil?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(perfil: Perfil)
}
