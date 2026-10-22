package com.example.apptiotec_n.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.apptiotec_n.model.Accion
import com.example.apptiotec_n.model.Evaluacion
import com.example.apptiotec_n.model.Perfil
import com.example.apptiotec_n.model.Respuesta

@Database(
    entities = [Perfil::class, Evaluacion::class, Respuesta::class, Accion::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun perfilDao(): PerfilDao
    abstract fun evaluacionDao(): EvaluacionDao
    abstract fun respuestaDao(): RespuestaDao
    abstract fun accionDao(): AccionDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        fun obtener(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tiotec.db"
                ).build().also { instancia = it }
            }
    }
}
