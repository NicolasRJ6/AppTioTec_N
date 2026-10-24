package com.example.apptiotec_n.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiotec_n.data.repository.TioTecRepository
import com.example.apptiotec_n.model.Perfil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = TioTecRepository(application)

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val perfil = repositorio.perfil.first()
            if (perfil != null) {
                _uiState.value = _uiState.value.copy(
                    alias = perfil.alias,
                    nivelExperiencia = perfil.nivelExperiencia,
                    dispositivo = perfil.dispositivo,
                    sistemaOperativo = perfil.sistemaOperativo,
                    imagenUri = perfil.imagenUri
                )
            }
        }
    }

    fun cambiarAlias(valor: String) {
        _uiState.value = _uiState.value.copy(
            alias = valor,
            errores = _uiState.value.errores.copy(alias = null)
        )
    }

    fun cambiarNivelExperiencia(valor: String) {
        _uiState.value = _uiState.value.copy(
            nivelExperiencia = valor,
            errores = _uiState.value.errores.copy(nivelExperiencia = null)
        )
    }

    fun cambiarDispositivo(valor: String) {
        _uiState.value = _uiState.value.copy(
            dispositivo = valor,
            errores = _uiState.value.errores.copy(dispositivo = null)
        )
    }

    fun cambiarSistemaOperativo(valor: String) {
        _uiState.value = _uiState.value.copy(
            sistemaOperativo = valor,
            errores = _uiState.value.errores.copy(sistemaOperativo = null)
        )
    }

    fun cambiarImagen(ruta: String) {
        _uiState.value = _uiState.value.copy(imagenUri = ruta)
    }

    fun validarFormulario(): Boolean {
        val estado = _uiState.value
        val errores = PerfilErrores(
            alias = when {
                estado.alias.isBlank() -> "El alias es obligatorio"
                estado.alias.trim().length < 3 -> "El alias debe tener al menos 3 caracteres"
                else -> null
            },
            nivelExperiencia = if (estado.nivelExperiencia.isBlank()) {
                "Selecciona tu nivel de experiencia"
            } else {
                null
            },
            dispositivo = if (estado.dispositivo.isBlank()) {
                "El dispositivo es obligatorio"
            } else {
                null
            },
            sistemaOperativo = if (estado.sistemaOperativo.isBlank()) {
                "El sistema operativo es obligatorio"
            } else {
                null
            }
        )

        _uiState.value = estado.copy(errores = errores)

        return errores.alias == null &&
            errores.nivelExperiencia == null &&
            errores.dispositivo == null &&
            errores.sistemaOperativo == null
    }

    fun guardarPerfil() {
        if (!validarFormulario()) return

        val estado = _uiState.value

        viewModelScope.launch {
            repositorio.guardarPerfil(
                Perfil(
                    alias = estado.alias.trim(),
                    nivelExperiencia = estado.nivelExperiencia,
                    dispositivo = estado.dispositivo.trim(),
                    sistemaOperativo = estado.sistemaOperativo.trim(),
                    imagenUri = estado.imagenUri
                )
            )
            _uiState.value = _uiState.value.copy(perfilGuardado = true)
        }
    }

    fun avisoMostrado() {
        _uiState.value = _uiState.value.copy(perfilGuardado = false)
    }
}
