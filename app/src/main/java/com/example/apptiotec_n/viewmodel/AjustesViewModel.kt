package com.example.apptiotec_n.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiotec_n.data.preference.AjustesRepository
import com.example.apptiotec_n.model.Ajustes
import com.example.apptiotec_n.notificaciones.mostrarRecordatorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AjustesViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = AjustesRepository(application)

    private val _uiState = MutableStateFlow(AjustesUiState())
    val uiState: StateFlow<AjustesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repositorio.ajustes.collect { ajustes ->
                _uiState.value = _uiState.value.copy(
                    recordatorioActivo = ajustes.recordatorioActivo,
                    horaRecordatorio = ajustes.horaRecordatorio
                )
            }
        }
    }

    fun cambiarRecordatorio(activo: Boolean) {
        _uiState.value = _uiState.value.copy(
            recordatorioActivo = activo,
            guardado = false
        )
    }

    fun cambiarHora(valor: String) {
        _uiState.value = _uiState.value.copy(
            horaRecordatorio = valor,
            errorHora = null,
            guardado = false
        )
    }

    fun validarFormulario(): Boolean {
        val hora = _uiState.value.horaRecordatorio.trim()
        val formato = Regex("^([01][0-9]|2[0-3]):[0-5][0-9]$")

        val error = when {
            hora.isEmpty() -> "La hora es obligatoria"
            !formato.matches(hora) -> "Usa el formato HH:mm, por ejemplo 20:00"
            else -> null
        }

        _uiState.value = _uiState.value.copy(errorHora = error)

        return error == null
    }

    fun guardar() {
        if (!validarFormulario()) return

        val estado = _uiState.value

        viewModelScope.launch {
            repositorio.guardar(
                Ajustes(
                    recordatorioActivo = estado.recordatorioActivo,
                    horaRecordatorio = estado.horaRecordatorio.trim()
                )
            )
            _uiState.value = _uiState.value.copy(guardado = true)
        }
    }

    fun avisoMostrado() {
        _uiState.value = _uiState.value.copy(guardado = false)
    }

    fun probarRecordatorio() {
        mostrarRecordatorio(
            getApplication(),
            _uiState.value.horaRecordatorio.trim()
        )
    }
}
