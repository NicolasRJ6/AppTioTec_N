package com.example.apptiotec_n.data.preference

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.apptiotec_n.model.Ajustes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "ajustes")

class AjustesRepository(private val context: Context) {

    private val claveRecordatorio = booleanPreferencesKey("recordatorio_activo")
    private val claveHora = stringPreferencesKey("hora_recordatorio")

    val ajustes: Flow<Ajustes> = context.dataStore.data.map { preferencias ->
        Ajustes(
            recordatorioActivo = preferencias[claveRecordatorio] ?: false,
            horaRecordatorio = preferencias[claveHora] ?: "20:00"
        )
    }

    suspend fun guardar(ajustes: Ajustes) {
        context.dataStore.edit { preferencias ->
            preferencias[claveRecordatorio] = ajustes.recordatorioActivo
            preferencias[claveHora] = ajustes.horaRecordatorio
        }
    }
}
