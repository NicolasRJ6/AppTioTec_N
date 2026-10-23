package com.example.apptiotec_n.viewmodel

data class EvaluacionUiState(
    val respuestas: Map<Int, Int> = emptyMap(),
    val errores: Set<Int> = emptySet(),
    val mensajeError: String? = null,
    val guardando: Boolean = false,
    val terminada: Boolean = false
) {
    val respondidas: Int get() = respuestas.size
    val total: Int get() = com.example.apptiotec_n.model.Pregunta.lista.size
    val progreso: Float get() = if (total == 0) 0f else respondidas.toFloat() / total
}
