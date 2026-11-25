package com.example.apptiotec_n.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiotec_n.data.model.ConsejoServidor
import com.example.apptiotec_n.data.repository.ConsejosServidorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConsejosServidorUiState(
    val cargando: Boolean = true,
    val guardando: Boolean = false,
    val consejos: List<ConsejoServidor> = emptyList(),
    val error: String? = null
)

/**
 * Estado de la pantalla de consejos del microservicio: listar, crear, editar y
 * eliminar contra la API Spring Boot. Después de cada cambio se vuelve a
 * consultar la lista para mostrar lo que quedó guardado en la base de datos.
 */
class ConsejosServidorViewModel(
    private val repositorio: ConsejosServidorRepository = ConsejosServidorRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConsejosServidorUiState())
    val uiState: StateFlow<ConsejosServidorUiState> = _uiState.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }

            runCatching { repositorio.listar() }
                .onSuccess { consejos ->
                    _uiState.update {
                        it.copy(cargando = false, consejos = consejos, error = null)
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(cargando = false, error = ERROR_CONEXION)
                    }
                }
        }
    }

    fun guardar(consejo: ConsejoServidor) {
        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true, error = null) }

            runCatching {
                if (consejo.id == 0L) {
                    repositorio.crear(consejo)
                } else {
                    repositorio.actualizar(consejo)
                }
                repositorio.listar()
            }
                .onSuccess { consejos ->
                    _uiState.update {
                        it.copy(guardando = false, consejos = consejos, error = null)
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(guardando = false, error = ERROR_GUARDAR)
                    }
                }
        }
    }

    fun eliminar(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(error = null) }

            runCatching {
                repositorio.eliminar(id)
                repositorio.listar()
            }
                .onSuccess { consejos ->
                    _uiState.update { it.copy(consejos = consejos, error = null) }
                }
                .onFailure {
                    _uiState.update { it.copy(error = ERROR_ELIMINAR) }
                }
        }
    }

    private companion object {
        const val ERROR_CONEXION =
            "No se pudo conectar con el microservicio. Revisa que esté en ejecución y que la URL sea accesible desde el teléfono."
        const val ERROR_GUARDAR =
            "No se pudo guardar el consejo. Intenta de nuevo."
        const val ERROR_ELIMINAR =
            "No se pudo eliminar el consejo. Intenta de nuevo."
    }
}
