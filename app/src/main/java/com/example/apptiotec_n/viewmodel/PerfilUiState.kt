package com.example.apptiotec_n.viewmodel

data class PerfilUiState(
    val alias: String = "",
    val nivelExperiencia: String = "",
    val dispositivo: String = "",
    val sistemaOperativo: String = "",
    val imagenUri: String? = null,
    val errores: PerfilErrores = PerfilErrores(),
    val mensajeError: String? = null,
    val perfilGuardado: Boolean = false
)

data class PerfilErrores(
    val alias: String? = null,
    val nivelExperiencia: String? = null,
    val dispositivo: String? = null,
    val sistemaOperativo: String? = null
)
