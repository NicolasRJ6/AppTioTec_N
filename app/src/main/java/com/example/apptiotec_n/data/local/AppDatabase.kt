package com.example.apptiotec_n.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.apptiotec_n.model.Accion
import com.example.apptiotec_n.model.Evaluacion
import com.example.apptiotec_n.model.Perfil
import com.example.apptiotec_n.model.Respuesta

@Database(
    entities = [Perfil::class, Evaluacion::class, Respuesta::class, Accion::class, ConsejoEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun perfilDao(): PerfilDao
    abstract fun evaluacionDao(): EvaluacionDao
    abstract fun respuestaDao(): RespuestaDao
    abstract fun accionDao(): AccionDao
    abstract fun consejoDao(): ConsejoDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        private val MIGRACION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `consejos` (" +
                        "`id` INTEGER NOT NULL, " +
                        "`titulo` TEXT NOT NULL, " +
                        "`descripcion` TEXT NOT NULL, " +
                        "`actualizadoEn` INTEGER NOT NULL, " +
                        "PRIMARY KEY(`id`))"
                )
            }
        }

        fun obtener(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tiotec.db"
                )
                    .addMigrations(MIGRACION_1_2)
                    .build()
                    .also { instancia = it }
            }
    }
}
