package com.example.apptiotec_n.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiotec_n.data.repository.TioTecRepository
import com.example.apptiotec_n.model.Accion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ResultadoUiState(
    val puntaje: Int = 0,
    val riesgo: String = "",
    val fecha: Long = 0,
    val acciones: List<Accion> = emptyList(),
    val cargando: Boolean = true
)

class ResultadoViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = TioTecRepository(application)

    private val _uiState = MutableStateFlow(ResultadoUiState())
    val uiState: StateFlow<ResultadoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val evaluacion = repositorio.ultimaEvaluacion.first()
            if (evaluacion == null) {
                _uiState.value = _uiState.value.copy(cargando = false)
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                puntaje = evaluacion.puntaje,
                riesgo = evaluacion.riesgo,
                fecha = evaluacion.fecha,
                cargando = false
            )

            repositorio.accionesDe(evaluacion.id).collect { acciones ->
                _uiState.value = _uiState.value.copy(acciones = acciones)
            }
        }
    }

    fun marcarAccion(id: Long, completada: Boolean) {
        viewModelScope.launch {
            repositorio.marcarAccion(id, completada)
        }
    }
}
