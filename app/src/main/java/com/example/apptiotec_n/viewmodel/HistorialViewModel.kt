package com.example.apptiotec_n.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiotec_n.data.repository.TioTecRepository
import com.example.apptiotec_n.model.Accion
import com.example.apptiotec_n.model.Evaluacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HistorialUiState(
    val evaluaciones: List<Evaluacion> = emptyList(),
    val cargando: Boolean = true,
    val idExpandido: Long? = null,
    val acciones: List<Accion> = emptyList()
) {
    val total: Int get() = evaluaciones.size
    val mejorPuntaje: Int get() = evaluaciones.maxOfOrNull { it.puntaje } ?: 0
    val promedio: Int
        get() = if (evaluaciones.isEmpty()) 0
        else evaluaciones.sumOf { it.puntaje } / evaluaciones.size
}

class HistorialViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = TioTecRepository(application)

    private val _uiState = MutableStateFlow(HistorialUiState())
    val uiState: StateFlow<HistorialUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repositorio.evaluaciones.collect { lista ->
                _uiState.value = _uiState.value.copy(
                    evaluaciones = lista,
                    cargando = false
                )
            }
        }
    }

    fun alternarDetalle(evaluacionId: Long) {
        if (_uiState.value.idExpandido == evaluacionId) {
            _uiState.value = _uiState.value.copy(idExpandido = null, acciones = emptyList())
            return
        }

        _uiState.value = _uiState.value.copy(idExpandido = evaluacionId, acciones = emptyList())

        viewModelScope.launch {
            repositorio.accionesDe(evaluacionId).collect { acciones ->
                if (_uiState.value.idExpandido == evaluacionId) {
                    _uiState.value = _uiState.value.copy(acciones = acciones)
                }
            }
        }
    }

    fun marcarAccion(id: Long, completada: Boolean) {
        viewModelScope.launch {
            repositorio.marcarAccion(id, completada)
        }
    }
}
