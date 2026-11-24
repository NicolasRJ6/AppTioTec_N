package com.example.apptiotec_n.data.repository

import android.content.Context
import com.example.apptiotec_n.data.local.AppDatabase
import com.example.apptiotec_n.data.local.ConsejoDao
import com.example.apptiotec_n.data.local.aEntidad
import com.example.apptiotec_n.data.local.aModelo
import com.example.apptiotec_n.data.model.Consejo
import com.example.apptiotec_n.data.remote.ApiService
import com.example.apptiotec_n.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConsejosRepository(
    private val consejoDao: ConsejoDao,
    private val api: ApiService = RetrofitInstance.api
) {

    constructor(context: Context) : this(
        consejoDao = AppDatabase.obtener(context).consejoDao()
    )

    fun observarConsejos(): Flow<List<Consejo>> =
        consejoDao.observarTodos().map { entidades ->
            entidades.map { it.aModelo() }
        }

    fun observarUltimaActualizacion(): Flow<Long?> =
        consejoDao.observarUltimaActualizacion()

    /**
     * Descarga los consejos desde la API y reemplaza la copia local en una sola
     * transacción, para que la pantalla siempre muestre datos completos.
     */
    suspend fun sincronizar(): Int {
        val remotos = api.obtenerConsejos()
        val ahora = System.currentTimeMillis()
        consejoDao.reemplazarTodos(remotos.map { it.aEntidad(ahora) })
        return remotos.size
    }
}
