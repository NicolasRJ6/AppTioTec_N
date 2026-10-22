package com.example.apptiotec_n.data.repository

import android.content.Context
import com.example.apptiotec_n.data.local.AppDatabase
import com.example.apptiotec_n.model.Accion
import com.example.apptiotec_n.model.Evaluacion
import com.example.apptiotec_n.model.Perfil
import com.example.apptiotec_n.model.Respuesta
import kotlinx.coroutines.flow.Flow

class TioTecRepository(context: Context) {

    private val baseDatos = AppDatabase.obtener(context)

    val perfil: Flow<Perfil?> = baseDatos.perfilDao().observarPerfil()
    val evaluaciones: Flow<List<Evaluacion>> = baseDatos.evaluacionDao().observarTodas()
    val ultimaEvaluacion: Flow<Evaluacion?> = baseDatos.evaluacionDao().observarUltima()

    suspend fun guardarPerfil(perfil: Perfil) {
        baseDatos.perfilDao().guardar(perfil)
    }

    suspend fun guardarEvaluacion(
        evaluacion: Evaluacion,
        respuestas: List<Respuesta>,
        acciones: List<Accion>
    ): Long {
        val id = baseDatos.evaluacionDao().insertar(evaluacion)
        baseDatos.respuestaDao().insertarTodas(respuestas.map { it.copy(evaluacionId = id) })
        baseDatos.accionDao().insertarTodas(acciones.map { it.copy(evaluacionId = id) })
        return id
    }

    suspend fun respuestasDe(evaluacionId: Long): List<Respuesta> =
        baseDatos.respuestaDao().obtenerDeEvaluacion(evaluacionId)

    fun accionesDe(evaluacionId: Long): Flow<List<Accion>> =
        baseDatos.accionDao().observarDeEvaluacion(evaluacionId)

    suspend fun marcarAccion(id: Long, completada: Boolean) {
        baseDatos.accionDao().actualizarCompletada(id, completada)
    }
}
