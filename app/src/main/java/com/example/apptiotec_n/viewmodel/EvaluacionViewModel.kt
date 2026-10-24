package com.example.apptiotec_n.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiotec_n.data.repository.TioTecRepository
import com.example.apptiotec_n.model.Accion
import com.example.apptiotec_n.model.Evaluacion
import com.example.apptiotec_n.model.Pregunta
import com.example.apptiotec_n.model.Respuesta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EvaluacionViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = TioTecRepository(application)

    private val _uiState = MutableStateFlow(EvaluacionUiState())
    val uiState: StateFlow<EvaluacionUiState> = _uiState.asStateFlow()

    fun responder(preguntaId: Int, valor: Int) {
        val estado = _uiState.value
        _uiState.value = estado.copy(
            respuestas = estado.respuestas + (preguntaId to valor),
            errores = estado.errores - preguntaId,
            mensajeError = null
        )
    }

    fun validarFormulario(): Boolean {
        val estado = _uiState.value
        val faltantes = Pregunta.lista
            .filter { !estado.respuestas.containsKey(it.id) }
            .map { it.id }
            .toSet()

        _uiState.value = estado.copy(
            errores = faltantes,
            mensajeError = when (faltantes.size) {
                0 -> null
                1 -> "Te falta 1 pregunta por responder"
                else -> "Te faltan ${faltantes.size} preguntas por responder"
            }
        )

        return faltantes.isEmpty()
    }

    fun finalizar() {
        if (!validarFormulario()) return

        val estado = _uiState.value
        val puntaje = estado.respuestas.values.sum()
        val riesgo = when {
            puntaje >= 80 -> "Bajo"
            puntaje >= 50 -> "Medio"
            else -> "Alto"
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(guardando = true)

            val respuestas = Pregunta.lista.map { pregunta ->
                Respuesta(
                    evaluacionId = 0,
                    preguntaId = pregunta.id,
                    categoria = pregunta.categoria,
                    valor = estado.respuestas[pregunta.id] ?: 0
                )
            }

            val acciones = Pregunta.lista
                .filter { (estado.respuestas[it.id] ?: 0) < 10 }
                .map { Accion(evaluacionId = 0, texto = it.recomendacion) }

            try {
                repositorio.guardarEvaluacion(
                    Evaluacion(fecha = System.currentTimeMillis(), puntaje = puntaje, riesgo = riesgo),
                    respuestas,
                    acciones
                )
                _uiState.value = _uiState.value.copy(guardando = false, terminada = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    guardando = false,
                    mensajeError = "No se pudo guardar la evaluación"
                )
            }
        }
    }

    fun reiniciar() {
        _uiState.value = EvaluacionUiState()
    }
}
