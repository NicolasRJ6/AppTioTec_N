package com.example.apptiotec_n.viewmodel

import com.example.apptiotec_n.data.model.Consejo

data class ConsejosUiState(
    val cargando: Boolean = true,
    val sincronizando: Boolean = false,
    val consejos: List<Consejo> = emptyList(),
    val actualizadoEn: Long? = null,
    val error: String? = null
)
