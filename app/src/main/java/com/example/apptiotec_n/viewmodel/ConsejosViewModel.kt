package com.example.apptiotec_n.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiotec_n.data.repository.ConsejosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConsejosViewModel(
    application: Application,
    private val repositorio: ConsejosRepository
) : AndroidViewModel(application) {

    constructor(application: Application) : this(
        application,
        ConsejosRepository(application)
    )

    private val _uiState = MutableStateFlow(ConsejosUiState())
    val uiState: StateFlow<ConsejosUiState> = _uiState.asStateFlow()

    init {
        observarCache()
        sincronizar()
    }

    private fun observarCache() {
        viewModelScope.launch {
            repositorio.observarConsejos().collect { consejos ->
                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    consejos = consejos
                )
            }
        }
        viewModelScope.launch {
            repositorio.observarUltimaActualizacion().collect { actualizadoEn ->
                _uiState.value = _uiState.value.copy(actualizadoEn = actualizadoEn)
            }
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(sincronizando = true, error = null)

            runCatching { repositorio.sincronizar() }
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        sincronizando = false,
                        error = null
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        sincronizando = false,
                        error = "No se pudieron actualizar los consejos. Revisa tu conexión."
                    )
                }
        }
    }
}
